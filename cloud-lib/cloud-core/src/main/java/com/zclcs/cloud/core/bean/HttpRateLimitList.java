package com.zclcs.cloud.core.bean;

import com.zclcs.common.core.constant.DatePattern;

import java.time.LocalTime;

/**
 * @author zclcs
 */
public class HttpRateLimitList {

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

    private Integer rateLimitCount;

    private Integer intervalSec;

    public HttpRateLimitList() {
    }

    public HttpRateLimitList(String method, String path, LocalTime limitFrom, LocalTime limitTo, Integer rateLimitCount, Integer intervalSec) {
        this.method = method;
        this.path = path;
        this.limitFrom = limitFrom;
        this.limitTo = limitTo;
        this.rateLimitCount = rateLimitCount;
        this.intervalSec = intervalSec;
    }

    public HttpRateLimitList(String method, String path, String limitFrom, String limitTo, Integer rateLimitCount, Integer intervalSec) {
        this.method = method;
        this.path = path;
        setLimitFrom(limitFrom);
        setLimitTo(limitTo);
        this.rateLimitCount = rateLimitCount;
        this.intervalSec = intervalSec;
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
        this.limitFrom = LocalTime.parse(limitFrom, DatePattern.TIME_FORMATTER);
    }

    public LocalTime getLimitTo() {
        return limitTo;
    }

    public void setLimitTo(String limitTo) {
        this.limitTo = LocalTime.parse(limitTo, DatePattern.TIME_FORMATTER);
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
