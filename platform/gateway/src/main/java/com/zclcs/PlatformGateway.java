package com.zclcs;

import com.zclcs.common.discovery.DiscoveryVerticle;
import com.zclcs.common.web.starter.WebStarterImpl;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.List;
import java.util.Optional;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class PlatformGateway extends DiscoveryVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformGateway.class);

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route("/api/*").handler(this::dispatchRequests);
        WebStarterImpl webStarter = new WebStarterImpl(vertx, router, "0.0.0.0", 8080);
        webStarter.setUp();
    }

    private void dispatchRequests(RoutingContext context) {
        // length of `/api/`
        int initialOffset = 5;
        // run with circuit breaker in order to deal with failure
        circuitBreaker.execute(future -> {
            List<Record> allEndpoints = getAllEndpoints();
            String path = context.request().uri();

            if (path.length() <= initialOffset) {
                notFound(context);
                future.complete();
                return;
            }
            String prefix = (path.substring(initialOffset)
                    .split("/"))[0];
            // generate new relative path
            String newPath = path.substring(initialOffset + prefix.length());
            // get one relevant HTTP client, may not exist
            Optional<Record> client = allEndpoints.stream()
                    .filter(record -> record.getMetadata().getString("api.name") != null)
                    .filter(record -> record.getMetadata().getString("api.name").equals(prefix))
                    .findAny(); // simple load balance

            if (client.isPresent()) {
                doDispatch(context, newPath, serviceDiscovery.getReference(client.get()).get());
                future.complete();
            } else {
                notFound(context);
                future.complete();
            }
        });
    }

    /**
     * Dispatch the request to the downstream REST layers.
     *
     * @param context routing context instance
     * @param path    relative path
     * @param client  relevant HTTP client
     */
    private void doDispatch(RoutingContext context, String path, HttpClient client) {
        HttpClientRequest req = await(client.request(context.request().method(), path));
        context.request().headers().forEach(header -> {
            req.putHeader(header.getKey(), header.getValue());
        });
        if (context.user() != null) {
            req.putHeader("user-principal", context.user().principal().encode());
        }
        req.end(context.body().buffer());
        HttpClientResponse resp = await(req.send());
        Buffer body = await(resp.body());
        // api endpoint server error, circuit breaker should fail
        context.response().setStatusCode(resp.statusCode());
        resp.headers().forEach(header -> {
            context.response().putHeader(header.getKey(), header.getValue());
        });
        context.response().end(body);
    }

    /**
     * Get all REST endpoints from the service discovery infrastructure.
     *
     * @return async result
     */
    private List<Record> getAllEndpoints() {
        return await(serviceDiscovery.getRecords(r -> r.getType().equals(HttpEndpoint.TYPE)));
    }

    private void notFound(RoutingContext context) {
        context.response().setStatusCode(404)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("message", "not_found").encodePrettily());
    }
}
