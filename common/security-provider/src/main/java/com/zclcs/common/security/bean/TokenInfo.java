package com.zclcs.common.security.bean;

import com.zclcs.common.security.constant.LoginDevice;
import com.zclcs.common.security.constant.LoginType;

/**
 * @author zclcs
 */
public class TokenInfo {

    private String loginId;

    private LoginType loginType;

    private LoginDevice loginDevice;

    public TokenInfo() {
    }

    public TokenInfo(String loginId, LoginType loginType, LoginDevice loginDevice) {
        this.loginId = loginId;
        this.loginType = loginType;
        this.loginDevice = loginDevice;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public LoginDevice getLoginDevice() {
        return loginDevice;
    }

    public void setLoginDevice(LoginDevice loginDevice) {
        this.loginDevice = loginDevice;
    }
}
