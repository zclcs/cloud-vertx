package com.zclcs;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.zclcs.bean.Res;
import com.zclcs.common.discovery.BaseDiscoveryVerticle;
import com.zclcs.common.web.starter.WebStarterImpl;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class PlatformGateway extends BaseDiscoveryVerticle {

    private static final int DEFAULT_PORT = 8101;
    private static final Logger log = LoggerFactory.getLogger(PlatformGateway.class);
    private WebStarterImpl webStart;
    private AsyncLoadingCache<String, List<Record>> recordsCache;
    private HttpClient client;

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
        client = vertx.createHttpClient();
        log.info("http server started on " + host + ":" + port);
        recordsCache = redisStarter.create(vertx.getOrCreateContext(), Duration.ofSeconds(30), this::getAllEndpoints);
    }

    private void dispatchRequests(RoutingContext context) {
        int initialOffset = 5;
        String path = context.request().uri();

        if (path.length() <= initialOffset) {
            resp(context, Res.notFound());
        }
        // 要转发的服务路径
        String prefix = (path.substring(initialOffset)
                .split("/"))[0];
        // 转发的相对路径
        String newPath = path.substring(initialOffset + prefix.length());
        String finalPrefix = "/" + prefix;
        String finalNewPath = "/" + newPath;

        List<Record> records = await(Future.fromCompletionStage(recordsCache.get("all")));
        // get one relevant HTTP client, may not exist
        Optional<Record> forward = records.stream()
                .filter(record -> record.getMetadata().getString("api.name") != null)
                .filter(record -> record.getMetadata().getString("api.name").equals(finalPrefix))
                .findAny();
        if (forward.isPresent()) {
            String host = forward.get().getLocation().getString("host");
            Integer port = forward.get().getLocation().getInteger("port");
            var req = await(client.request(context.request().method(), port, host, finalNewPath));
            var resp = await(req.send());
            var status = resp.statusCode();
            var body = await(resp.body());
            Res res = new Res(status, resp.headers(), body);
            resp(context, res);
        } else {
            resp(context, Res.serviceUnavailable());
        }
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
