package com.zclcs.cloud.core.bean;

import io.vertx.core.http.HttpMethod;

/**
 * @author zclcs
 */
public class HttpWhiteList {

    /**
     * 允许的http方法
     */
    private HttpMethod method;

    /**
     * 允许的路径
     */
    private String path;

    public HttpWhiteList(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
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
}
