package com.zclcs.cloud.security;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.security.provider.PermissionProvider;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author zclcs
 */
public abstract class BasePermissionHandler implements Handler<RoutingContext> {

    protected final PermissionProvider permissionProvider;

    public BasePermissionHandler(PermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String loginId = ctx.get(SecurityContext.LOGIN_ID);
        String loginType = ctx.get(SecurityContext.LOGIN_TYPE, "PC");
        if (loginId == null || loginType == null) {
            RoutingContextUtil.error(ctx, HttpStatus.HTTP_FAILED_DEPENDENCY, "未登录");
        } else {
            permissionProvider.hasPermission(loginId, loginType)
                    .onComplete(res -> {
                        if (res) {
                            doNext(ctx);
                        } else {
                            RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, "无权限");
                        }
                    }, e -> {
                        RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNAUTHORIZED, "无权限");
                    })
            ;
        }
    }

    /**
     * 校验完权限下一步处理
     *
     * @param ctx 上下文
     */
    public abstract void doNext(RoutingContext ctx);

}
