package com.zclcs.common.security.provider;

import io.vertx.core.Future;

/**
 * @author zclcs
 */
public interface TokenProvider {

    /**
     * 生成token并存储
     *
     * @param loginId   用户实体（用户名、手机号等等）
     * @param loginType 登录类型（pc、app等等）
     * @return token
     */
    Future<String> generateAndStoreToken(String loginId, String loginType);

    /**
     * 验证token
     *
     * @param token
     * @return 用户实体（用户名、手机号等等）
     */
    Future<String> verifyToken(String token);

}
