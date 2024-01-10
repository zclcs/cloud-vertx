package com.zclcs.platform.system.dao.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户 Vo
 *
 * @author zclcs
 * @since 2023-01-10 10:39:34.182
 */
public class UserTokenVo implements Serializable {

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
    private LoginVo userinfo;

    public UserTokenVo(String token, Long expire, LoginVo userinfo) {
        this.token = token;
        this.expire = expire;
        this.userinfo = userinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public LoginVo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(LoginVo userinfo) {
        this.userinfo = userinfo;
    }
}