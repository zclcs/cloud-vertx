package com.zclcs.platform.system.handler.user;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.core.constant.StringPool;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.service.UserService;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author zclcs
 */
public class DeleteUserHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    public DeleteUserHandler(PermissionProvider permissionProvider, UserService userService) {
        super(permissionProvider);
        this.userService = userService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        String userIds = ctx.pathParam("userIds");
        List<Long> userIdList = Arrays.stream(userIds.split(StringPool.COMMA)).map(Long::parseLong).toList();
        userService.deleteUser(userIdList)
                .onComplete(userVoPage -> {
                    RoutingContextUtil.success(ctx, userVoPage);
                }, e -> {
                    log.error("查询失败", e);
                    RoutingContextUtil.error(ctx, "查询失败");
                })
        ;
    }


}
