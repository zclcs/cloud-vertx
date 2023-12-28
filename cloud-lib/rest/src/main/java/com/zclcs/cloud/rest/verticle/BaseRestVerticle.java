package com.zclcs.cloud.rest.verticle;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.cloud.core.verticle.BaseVerticle;
import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.rest.bean.HttpWhiteList;
import com.zclcs.cloud.rest.utils.RoutingContextUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.redis.starter.RedisStarter;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.redis.client.RedisAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public abstract class BaseRestVerticle extends BaseVerticle {

    private final Logger log = LoggerFactory.getLogger(BaseRestVerticle.class);

    protected Router router;

    protected TokenProvider tokenProvider;

    protected RedisAPI redis;

    protected HttpServer httpServer;

    protected List<HttpWhiteList> whiteList = new ArrayList<>();

    @Override
    public void start() throws Exception {
        super.start();
        RedisStarter redisStarter = new RedisStarter(vertx, config);
        redis = redisStarter.connectRedis();
        tokenProvider = new RedisTokenLogic(redis);
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/*").handler(ctx -> {
            HttpMethod method = ctx.request().method();
            String path = ctx.request().path();

            for (HttpWhiteList whiteList : whiteList) {
                if (method.name().equals(method.name()) && path.equals(whiteList.getPath())) {
                    ctx.next();
                }
            }
            HttpServerRequest request = ctx.request();
            String s = request.headers().get("token");
            if (StringsUtil.isBlank(s)) {
                RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, HttpResult.msg("未鉴权"));
            } else {
                Boolean verifyToken = await(tokenProvider.verifyToken(s));
                if (verifyToken) {
                    ctx.next();
                } else {
                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, HttpResult.msg("token已过期"));
                }
            }
        });
        router.route().failureHandler((ctx) -> {
            log.error("failureHandler", ctx.failure());
            RoutingContextUtil.error(ctx, HttpResult.msg("服务器内部异常"));
        });
        httpServer = vertx.createHttpServer();
        httpServer.exceptionHandler((e) -> log.error("http server exception", e));
    }

    protected void addWhiteList(HttpWhiteList whiteList) {
        this.whiteList.add(whiteList);
    }

    protected void addRoute(HttpMethod method, String path, Handler<RoutingContext> requestHandler) {
        this.router.route().handler(requestHandler);
    }
}
