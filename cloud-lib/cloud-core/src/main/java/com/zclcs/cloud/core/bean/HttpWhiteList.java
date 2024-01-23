package com.zclcs.cloud.core.bean;

/**
 * @author zclcs
 */
public class HttpWhiteList {

    /**
     * 允许的http方法
     */
    private String method;

    /**
     * 允许的路径
     */
    private String path;

    public HttpWhiteList() {
    }

    public HttpWhiteList(String method, String path) {
        this.method = method;
        this.path = path;
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
}
