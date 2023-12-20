package com.zclcs;

import com.zclcs.common.discovery.BaseDiscoveryVerticle;
import com.zclcs.common.web.starter.WebStarterImpl;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.List;
import java.util.Optional;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class PlatformGateway extends BaseDiscoveryVerticle {

    private static final int DEFAULT_PORT = 8787;
    private static final Logger log = LoggerFactory.getLogger(PlatformGateway.class);
    private WebStarterImpl webStart;

    @Override
    public void start() throws Exception {
        super.start();
        ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("env");
        ConfigRetriever retriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));
        JsonObject config = await(retriever.getConfig());
        String host = config.getString("API_GATEWAY_HTTP_ADDRESS", "localhost");
        int port = config.getInteger("API_GATEWAY_HTTP_PORT", DEFAULT_PORT);

        Router router = Router.router(vertx);

        // body handler
        router.route().handler(BodyHandler.create());

        // api dispatcher
        router.route("/api/*").handler(this::dispatchRequests);

        webStart = new WebStarterImpl(vertx, router, host, port);
        webStart.setUp();
        log.info("API Gateway is running on port " + port);
    }

    private void dispatchRequests(RoutingContext context) {
        // length of `/api/`
        int initialOffset = 5;
        // run with circuit breaker in order to deal with failure
        vertxInternal.promise();
        circuitBreaker.execute(future -> {
            getAllEndpoints().onComplete(ar -> {
                if (ar.succeeded()) {
                    List<Record> recordList = ar.result();
                    // get relative path and retrieve prefix to dispatch client
                    String path = context.request().uri();

                    if (path.length() <= initialOffset) {
                        webStart.notFound(context);
                        future.complete();
                        return;
                    }
                    String prefix = "/" + (path.substring(initialOffset)
                            .split("/"))[0];
                    // generate new relative path
                    String newPath = "/" + path.substring(initialOffset + prefix.length());
                    // get one relevant HTTP client, may not exist
                    Optional<Record> client = recordList.stream()
                            .filter(record -> record.getMetadata().getString("api.name") != null)
                            .filter(record -> record.getMetadata().getString("api.name").equals(prefix))
                            .findAny(); // simple load balance

                    if (client.isPresent()) {
                        doDispatch(context, newPath, discovery.getReference(client.get()).get(), future);
                    } else {
                        webStart.notFound(context);
                        future.complete();
                    }
                } else {
                    future.fail(ar.cause());
                }
            });
        }).onFailure(ar -> {
            webStart.badGateway(context);
        });
    }

    /**
     * Dispatch the request to the downstream REST layers.
     *
     * @param context routing context instance
     * @param path    relative path
     * @param client  relevant HTTP client
     */
    private <T> void doDispatch(RoutingContext context, String path, HttpClient client, Promise<T> cbFuture) {
        HttpClientRequest toReq = await(client
                .request(context.request().method(), path));
        // set headers
        context.request().headers().forEach(header -> {
            toReq.putHeader(header.getKey(), header.getValue());
        });
        if (context.user() != null) {
            toReq.putHeader("user-principal", context.user().principal().encode());
        }
        // send request
        if (context.body().isEmpty()) {
            toReq.end();
        } else {
            toReq.end(context.body().buffer());
        }
        HttpClientResponse response = await(toReq.send());
        Buffer body = await(response.body());
        // api endpoint server error, circuit breaker should fail
        if (response.statusCode() >= 500) {
            cbFuture.fail(response.statusCode() + ": " + body.toString());
        } else {
            HttpServerResponse toRsp = context.response()
                    .setStatusCode(response.statusCode());
            response.headers().forEach(header -> {
                toRsp.putHeader(header.getKey(), header.getValue());
            });
            // send response
            toRsp.end(body);
            cbFuture.complete();
        }
        ServiceDiscovery.releaseServiceObject(discovery, client);
    }

    private void apiVersion(RoutingContext context) {
        context.response()
                .end(new JsonObject().put("version", "v1").encodePrettily());
    }

    /**
     * Get all REST endpoints from the service discovery infrastructure.
     *
     * @return async result
     */
    private Future<List<Record>> getAllEndpoints() {
        Promise<List<Record>> promise = vertxInternal.promise();
        discovery.getRecords(record -> record.getType().equals(HttpEndpoint.TYPE), promise);
        return promise.future();
    }

    private void logoutHandler(RoutingContext context) {
        context.clearUser();
        context.session().destroy();
        context.response().setStatusCode(204).end();
    }

    private String buildHostURI() {
        int port = config().getInteger("api.gateway.http.port", DEFAULT_PORT);
        final String host = config().getString("api.gateway.http.address.external", "localhost");
        return String.format("https://%s:%d", host, port);
    }
}
