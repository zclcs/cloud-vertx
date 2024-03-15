package com.zclcs.platform.system.handler.role;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.service.RoleService;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleOptionsHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RoleService roleService;

    public RoleOptionsHandler(PermissionProvider permissionProvider, RoleService roleService) {
        super(permissionProvider);
        this.roleService = roleService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        roleService.getRoleOptions().onComplete(roleOptions -> {
            RoutingContextUtil.success(ctx, roleOptions);
        }, e -> {
            log.error("获取角色选项失败", e);
            RoutingContextUtil.error(ctx, "获取角色选项失败");
        });
    }
}
