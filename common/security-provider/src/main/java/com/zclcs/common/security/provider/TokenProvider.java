package com.zclcs.common.security.provider;

import com.zclcs.common.security.bean.TokenInfo;
import com.zclcs.common.security.constant.LoginDevice;
import com.zclcs.common.security.constant.LoginType;
import io.vertx.core.Future;

import java.time.Duration;

/**
 * @author zclcs
 */
public interface TokenProvider {

    /**
     * 生成token并存储
     *
     * @param loginId     登录id 值可能是用户名或手机号等等
     * @param loginType   登录类型 {@link LoginType}
     * @param loginDevice 登录设备 {@link LoginDevice}
     * @return token
     */
    Future<String> generateAndStoreToken(String loginId, LoginType loginType, LoginDevice loginDevice);

    /**
     * 验证token
     *
     * @param token token
     * @return {@link TokenInfo}
     */
    Future<TokenInfo> verifyToken(String token);

    /**
     * token过期时间
     *
     * @return token过期时间
     */
    Duration getRedisTokenExpire();

}
