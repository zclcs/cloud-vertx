package com.zclcs.common.security.provider;

import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface PermissionProvider {

    Future<List<String>> hasPermission(String loginId, String loginType, String permission);

    Future<List<String>> hasRole(String loginId, String loginType, String role);

}
