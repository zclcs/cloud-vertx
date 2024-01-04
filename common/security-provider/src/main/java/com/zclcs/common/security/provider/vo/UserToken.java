package com.zclcs.common.security.provider.vo;

import io.vertx.core.json.JsonObject;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户 Vo
 *
 * @author zclcs
 * @since 2023-01-10 10:39:34.182
 */
public class UserToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户token
     */
    private String token;

    /**
     * 当前会话 token 剩余有效时间（单位: 秒，返回 -1 代表永久有效）
     */
    private Long expire;

    /**
     * 用户信息
     */
    private JsonObject userinfo;


}