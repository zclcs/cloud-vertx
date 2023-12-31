package com.zclcs.platform.system;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.rest.bean.HttpWhiteList;
import com.zclcs.cloud.rest.utils.RoutingContextUtil;
import com.zclcs.common.config.starter.ConfigStarter;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.redis.starter.RedisStarter;
import com.zclcs.common.security.provider.TokenProvider;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.redis.client.RedisAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends AbstractVerticle {

    private static final int DEFAULT_PORT = 8201;

    private final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    protected Router router;

    protected TokenProvider tokenProvider;

    protected RedisAPI redis;

    protected HttpServer httpServer;

    protected List<HttpWhiteList> whiteList = new ArrayList<>();

    protected JsonObject config;

    protected RedisStarter redisStarter;

    @Override
    public Completable rxStart() {
        ConfigStarter configStarter = new ConfigStarter(vertx);
        return configStarter.config().doOnSuccess(jsonObject -> {
                    config = jsonObject;
                }).ignoreElement()
                .andThen(Completable.defer(() -> {
                    redisStarter = new RedisStarter(vertx, config);
                    return redisStarter.connectRedis()
                            .doOnError(e -> {
                                log.error("connect redis error {}", e.getMessage(), e);
                                vertx.rxClose().subscribe();
                            })
                            .doOnComplete(() -> redis = RedisAPI.api(redisStarter.getClient()));
                }))
                .andThen(Completable.defer(() -> {
                    tokenProvider = new RedisTokenLogic(redis, context);
                    return Completable.complete();
                }))
                .andThen(Completable.fromAction(this::addAuthFilter))
                .andThen(Completable.defer(this::setUpHttpServer))
                ;
    }

    @Override
    public Completable rxStop() {
        return vertx.rxExecuteBlocking((Callable<Void>) () -> {
            if (redisStarter.getClient() != null) {
                redis.close();
                redisStarter.getClient().close();
            }
            return null;
        }).ignoreElement();
    }

    protected void addWhiteList(HttpWhiteList whiteList) {
        this.whiteList.add(whiteList);
    }

    protected void addRoute(HttpMethod method, String path, Handler<RoutingContext> requestHandler) {
        this.router.route().handler(requestHandler);
    }

    private void addAuthFilter() {
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
                tokenProvider.verifyToken(s).doOnSuccess(c -> {
                    if (c != null) {
                        ctx.next();
                    } else {
                        RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, HttpResult.msg("token已过期"));
                    }
                }).subscribe();
            }
        });
        router.route().failureHandler((ctx) -> {
            log.error("failureHandler", ctx.failure());
            RoutingContextUtil.error(ctx, HttpResult.msg("服务器内部异常"));
        });
    }

    private Completable setUpHttpServer() {
        httpServer = vertx.createHttpServer(new HttpServerOptions()
                .setTcpFastOpen(true)
                .setTcpCork(true)
                .setTcpQuickAck(true)
                .setReusePort(true)
        );
        httpServer.exceptionHandler((e) -> log.error("http server exception", e));
        String host = config.getString("PLATFORM_SYSTEM_HTTP_ADDRESS", "0.0.0.0");
        int port = config.getInteger("PLATFORM_SYSTEM_HTTP_PORT", 8201);
        addRoute(HttpMethod.GET, "/health", ctx -> RoutingContextUtil.success(ctx, HttpResult.msg("UP")));
        httpServer.requestHandler(router);
        return httpServer.rxListen(port, host).ignoreElement();
    }

}
