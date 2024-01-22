package com.zclcs.cloud.core.bean;

import io.vertx.core.http.HttpMethod;

/**
 * @author zclcs
 */
public class HttpBlackList {

    private String ip;

    /**
     * 允许的http方法
     */
    private HttpMethod method;

    /**
     * 允许的路径
     */
    private String path;

    private String limitFrom;

    private String limitTo;

    public HttpBlackList() {
    }

    public HttpBlackList(String ip, HttpMethod method, String path, String limitFrom, String limitTo) {
        this.ip = ip;
        this.method = method;
        this.path = path;
        this.limitFrom = limitFrom;
        this.limitTo = limitTo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLimitFrom() {
        return limitFrom;
    }

    public void setLimitFrom(String limitFrom) {
        this.limitFrom = limitFrom;
    }

    public String getLimitTo() {
        return limitTo;
    }

    public void setLimitTo(String limitTo) {
        this.limitTo = limitTo;
    }
}
