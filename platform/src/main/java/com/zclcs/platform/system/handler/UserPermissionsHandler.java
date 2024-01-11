package com.zclcs.platform.system.handler;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.SecurityContext;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class UserPermissionsHandler implements Handler<RoutingContext> {


    private final UserService userService;

    public UserPermissionsHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        userService.getUserPermissionCache(ctx.get(SecurityContext.LOGIN_ID))
                .onComplete(userPermission -> {
                    RoutingContextUtil.success(ctx, userPermission);
                }, e -> {
                    RoutingContextUtil.error(ctx, "获取用户权限失败");
                })
        ;
    }
}
