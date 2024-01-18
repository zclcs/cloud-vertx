package com.zclcs.platform.system.security;

import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Future;

/**
 * @author zclcs
 */
public class HasPermissionLogic implements PermissionProvider {

    private final String permission;
    private final UserService userService;

    public HasPermissionLogic(String permission, UserService userService) {
        this.permission = permission;
        this.userService = userService;
    }

    @Override
    public Future<Boolean> hasPermission(String loginId, String loginType) {
        return userService.getUserPermissionCache(loginId)
                .compose(userPermissions -> {
                    if (userPermissions != null && userPermissions.contains(permission)) {
                        return Future.succeededFuture(true);
                    } else {
                        return Future.succeededFuture(false);
                    }
                });
    }
}
