package com.zclcs.cloud.core.bean;

/**
 * @author zclcs
 */
public class HttpWhiteList {

    /**
     * 允许的ip
     */
    private String ip;

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

    public HttpWhiteList(String ip, String method, String path) {
        this.ip = ip;
        this.method = method;
        this.path = path;
    }

    public HttpWhiteList(String method, String path) {
        this.method = method;
        this.path = path;
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
}
