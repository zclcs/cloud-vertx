package com.zclcs.common.security.provider;

import com.zclcs.common.security.constant.LoginType;
import io.vertx.core.Future;

/**
 * @author zclcs
 */
public interface PermissionProvider {

    /**
     * 检查用户是否有权限
     *
     * @param loginId   登录id
     * @param loginType 登录类型
     * @return true:有权限
     */
    Future<Boolean> hasPermission(String loginId, LoginType loginType);
}
