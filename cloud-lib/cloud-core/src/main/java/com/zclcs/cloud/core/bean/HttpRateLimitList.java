package com.zclcs.cloud.core.bean;

import io.vertx.core.http.HttpMethod;

/**
 * @author zclcs
 */
public class HttpRateLimitList {

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

    private Integer rateLimitCount;

    private Integer intervalSec;

    public HttpRateLimitList() {
    }

    public HttpRateLimitList(HttpMethod method, String path, String limitFrom, String limitTo, Integer rateLimitCount, Integer intervalSec) {
        this.method = method;
        this.path = path;
        this.limitFrom = limitFrom;
        this.limitTo = limitTo;
        this.rateLimitCount = rateLimitCount;
        this.intervalSec = intervalSec;
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

    public Integer getRateLimitCount() {
        return rateLimitCount;
    }

    public void setRateLimitCount(Integer rateLimitCount) {
        this.rateLimitCount = rateLimitCount;
    }

    public Integer getIntervalSec() {
        return intervalSec;
    }

    public void setIntervalSec(Integer intervalSec) {
        this.intervalSec = intervalSec;
    }
}
