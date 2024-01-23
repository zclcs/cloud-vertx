package com.zclcs.cloud.core.bean;

import com.zclcs.common.core.constant.DatePattern;
import com.zclcs.common.core.utils.StringsUtil;

import java.time.LocalTime;

/**
 * @author zclcs
 */
public class HttpBlackList {

    private String ip;

    /**
     * 允许的http方法
     */
    private String method;

    /**
     * 允许的路径
     */
    private String path;

    private LocalTime limitFrom;

    private LocalTime limitTo;

    public HttpBlackList() {
    }

    public HttpBlackList(String ip, String method, String path, LocalTime limitFrom, LocalTime limitTo) {
        this.ip = ip;
        this.method = method;
        this.path = path;
        this.limitFrom = limitFrom;
        this.limitTo = limitTo;
    }

    public HttpBlackList(String ip, String method, String path, String limitFrom, String limitTo) {
        this.ip = ip;
        this.method = method;
        this.path = path;
        setLimitFrom(limitFrom);
        setLimitTo(limitTo);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalTime getLimitFrom() {
        return limitFrom;
    }

    public void setLimitFrom(String limitFrom) {
        this.limitFrom = StringsUtil.isBlank(limitFrom) ? null : LocalTime.parse(limitFrom, DatePattern.TIME_FORMATTER);
    }

    public LocalTime getLimitTo() {
        return limitTo;
    }

    public void setLimitTo(String limitTo) {
        this.limitTo = StringsUtil.isBlank(limitTo) ? null : LocalTime.parse(limitTo, DatePattern.TIME_FORMATTER);
    }
}