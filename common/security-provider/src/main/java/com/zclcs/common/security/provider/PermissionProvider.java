package com.zclcs.common.security.provider;

import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface PermissionProvider {

    Future<List<String>> getPermissions(String loginId, String loginType);

    Future<List<String>> getRoles(String loginId, String loginType);

}
