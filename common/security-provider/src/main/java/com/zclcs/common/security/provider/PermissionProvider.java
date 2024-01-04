package com.zclcs.common.security.provider;

import io.vertx.core.Future;

/**
 * @author zclcs
 */
public interface PermissionProvider {

    Future<Boolean> hasPermission(String loginId, String loginType, String permission);

    Future<Boolean> hasAnyPermission(String loginId, String loginType, String... permissions);

    Future<Boolean> hasRole(String loginId, String loginType, String role);

    Future<Boolean> hasAnyRole(String loginId, String loginType, String... roles);

}
