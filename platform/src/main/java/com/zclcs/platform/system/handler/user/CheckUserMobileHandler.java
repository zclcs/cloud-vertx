package com.zclcs.platform.system.handler.user;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.service.UserService;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class CheckUserMobileHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    public CheckUserMobileHandler(PermissionProvider permissionProvider, UserService userService) {
        super(permissionProvider);
        this.userService = userService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        Long userId = Long.valueOf(ctx.request().getParam("userId", "0"));
        String mobile = ctx.request().getParam("mobile");
        userService.validateMobile(mobile, userId)
                .onComplete(userVoPage -> {
                    RoutingContextUtil.success(ctx, userVoPage);
                }, e -> {
                    log.error("查询失败", e);
                    RoutingContextUtil.error(ctx, "查询失败");
                })
        ;
    }
}
