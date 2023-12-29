package com.zclcs.platform.system;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.cloud.rest.utils.RoutingContextUtil;
import com.zclcs.cloud.rest.verticle.BaseRestVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends BaseRestVerticle {

    private static final int DEFAULT_PORT = 8201;
    private static final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        String host = config.getString("PLATFORM_SYSTEM_HTTP_ADDRESS", "0.0.0.0");
        int port = config.getInteger("PLATFORM_SYSTEM_HTTP_PORT", DEFAULT_PORT);
        addRoute(HttpMethod.GET, "/health", ctx -> RoutingContextUtil.success(ctx, HttpResult.msg("UP")));
        httpServer.requestHandler(router);
        httpServer.listen(port, host).onComplete(ar -> {
            if (ar.succeeded()) {
                startPromise.complete();
                log.info("http server started on " + host + ":" + port);
            } else {
                startPromise.fail(ar.cause());
            }
        });
    }

}
