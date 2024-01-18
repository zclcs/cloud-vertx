package com.zclcs.common.security.provider;

import io.vertx.core.Future;

/**
 * @author zclcs
 */
public interface PermissionProvider {

    Future<Boolean> hasPermission(String loginId, String loginType);

}
