package com.zclcs.cloud.rest.verticle;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.cloud.core.verticle.BaseVerticle;
import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.rest.utils.RoutingContextUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.redis.starter.RedisStarter;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.redis.client.RedisAPI;

/**
 * @author zclcs
 */
public abstract class BaseRestVerticle extends BaseVerticle {

    protected Router router;

    protected TokenProvider tokenProvider;

    protected RedisAPI redis;

    @Override
    public void start() throws Exception {
        super.start();
        RedisStarter redisStarter = new RedisStarter(vertx, config);
        redis = redisStarter.connectRedis();
        tokenProvider = new RedisTokenLogic(redis);
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route("/**").handler(ctx -> {
            // TODO 白名单
            HttpServerRequest request = ctx.request();
            String s = request.headers().get("token");
            if (StringsUtil.isBlank(s)) {
                RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, HttpResult.msg("token已过期"));
            } else {
                tokenProvider.verifyToken(s);
            }
        });
        router.route().failureHandler((ctx) -> RoutingContextUtil.error(ctx, HttpResult.msg("服务器内部异常")));
    }
}
