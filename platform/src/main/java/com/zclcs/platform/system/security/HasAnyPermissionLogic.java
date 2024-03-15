package com.zclcs.platform.system.security;

import com.zclcs.common.security.constant.LoginType;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Future;

/**
 * @author zclcs
 */
public class HasAnyPermissionLogic implements PermissionProvider {

    private final UserService userService;
    private final String[] permission;

    public HasAnyPermissionLogic(UserService userService, String... permission) {
        this.userService = userService;
        this.permission = permission;
    }

    @Override
    public Future<Boolean> hasPermission(String loginId, LoginType loginType) {
        return userService.getUserPermissionCache(loginId)
                .compose(userPermissions -> {
                    if (userPermissions != null) {
                        for (String permission : permission) {
                            if (userPermissions.contains(permission)) {
                                return Future.succeededFuture(true);
                            }
                        }
                    }
                    return Future.succeededFuture(false);
                });
    }
}
