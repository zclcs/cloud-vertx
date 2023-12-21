package com.zclcs;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.zclcs.bean.Res;
import com.zclcs.common.discovery.BaseDiscoveryVerticle;
import com.zclcs.common.web.starter.WebStarterImpl;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class PlatformGateway extends BaseDiscoveryVerticle {

    private static final int DEFAULT_PORT = 8787;
    private static final Logger log = LoggerFactory.getLogger(PlatformGateway.class);
    private WebStarterImpl webStart;
    private AsyncLoadingCache<String, List<Record>> recordsCache;

    @Override
    public void start() throws Exception {
        super.start();
        String host = config.getString("PLATFORM_GATEWAY_HTTP_ADDRESS", "0.0.0.0");
        int port = config.getInteger("PLATFORM_GATEWAY_HTTP_PORT", DEFAULT_PORT);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create().setBodyLimit(1024 * 1024 * 10));

        // api dispatcher
        router.route("/api/*").handler(this::dispatchRequests);

        webStart = new WebStarterImpl(vertx, router, host, port);
        webStart.setUp();
        log.info("http server started on " + host + ":" + port);
        recordsCache = redisStarter.create(vertx.getOrCreateContext(), Duration.ofSeconds(30), this::getAllEndpoints);
    }

    private void dispatchRequests(RoutingContext context) {
        // 转发路径/api/的长度
        int initialOffset = 5;
        String path = context.request().uri();

        if (path.length() <= initialOffset) {
            resp(context, Res.notFound());
            return;
        }
        // 要转发的服务路径
        String prefix = (path.substring(initialOffset)
                .split("/"))[0];
        // 转发的相对路径
        String newPath = path.substring(initialOffset + prefix.length());
        String finalPrefix = "/" + prefix;
        String finalNewPath = "/" + newPath;
        circuitBreaker.executeWithFallback(
                        promise -> {

                            List<Record> records = await(Future.fromCompletionStage(recordsCache.get("all")));
                            // get one relevant HTTP client, may not exist
                            Optional<Record> client = records.stream()
                                    .filter(record -> record.getMetadata().getString("api.name") != null)
                                    .filter(record -> record.getMetadata().getString("api.name").equals(finalPrefix))
                                    .findAny();

                            if (client.isPresent()) {
                                ServiceReference reference = discovery.getReference(client.get());
                                HttpClient forward = reference.getAs(HttpClient.class);
                                // send request to relevant HTTP client
                                forward.request(context.request().method(), finalNewPath)
                                        .compose(req -> {

                                            context.request().headers().forEach(req::putHeader);
                                            MultiMap headers = context.request().headers();
                                            for (Map.Entry<String, String> header : headers) {
                                                req.putHeader(header.getKey(), header.getValue());
                                            }
                                            if (context.body().isEmpty()) {
                                                req.end();
                                            } else {
                                                req.end(context.body().buffer());
                                            }
                                            return req.send();
                                        })
                                        .compose(resp -> Future.succeededFuture(new Res(resp))).onComplete(promise);
                                ServiceDiscovery.releaseServiceObject(discovery, client);
                            } else {
                                Future.succeededFuture(Res.serviceUnavailable()).onComplete(promise);
                            }
                        }, v -> {
                            log.error("Circuit breaker is open", v);
                            // Executed when the circuit is opened
                            return Res.serviceUnavailable();
                        })
                .onComplete(res -> {
                    if (res.succeeded()) {
                        resp(context, res.result());
                    } else {
                        resp(context, Res.serviceUnavailable());
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
        }
        ServiceDiscovery.releaseServiceObject(discovery, client);
        cbFuture.complete();
    }

    /**
     * Get all REST endpoints from the service discovery infrastructure.
     *
     * @return async result
     */
    private Future<List<Record>> getAllEndpoints(String key) {
        Promise<List<Record>> promise = vertxInternal.promise();
        discovery.getRecords(record -> record.getType().equals(HttpEndpoint.TYPE), promise);
        log.info("Get all endpoints from service discovery");
        return promise.future();
    }

    public void resp(RoutingContext routingContext, Res res) {
        HttpServerResponse response = routingContext.response();
        response.setStatusCode(res.getStatusCode());
        res.getHead().forEach(header -> {
            response.putHeader(header.getKey(), header.getValue());
        });
        routingContext.end(res.getBody());
    }
}
