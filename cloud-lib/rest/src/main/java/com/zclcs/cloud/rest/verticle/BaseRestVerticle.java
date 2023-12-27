package com.zclcs.cloud.rest.verticle;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.cloud.core.verticle.BaseVerticle;
import com.zclcs.cloud.lib.security.login.LoginProvider;
import com.zclcs.cloud.lib.security.token.TokenProvider;
import com.zclcs.cloud.rest.utils.RoutingContextUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author zclcs
 */
public abstract class BaseRestVerticle extends BaseVerticle {

    protected Router router;

    private TokenProvider tokenProvider;

    @Override
    public void start() throws Exception {
        super.start();
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
