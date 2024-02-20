package com.zclcs.platform.system.handler.user;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.SecurityContext;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class UserRoutersHandler implements Handler<RoutingContext> {


    private final UserService userService;

    public UserRoutersHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        userService.getUserRouterCache(ctx.get(SecurityContext.LOGIN_ID))
                .onComplete(userRouters -> {
                    RoutingContextUtil.success(ctx, userRouters);
                }, e -> {
                    RoutingContextUtil.error(ctx, "获取用户路由失败");
                })
        ;
    }
}
