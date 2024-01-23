package com.zclcs.cloud.core.bean;

import com.zclcs.common.core.constant.DatePattern;

import java.time.LocalDateTime;

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

    private LocalDateTime limitFrom;

    private LocalDateTime limitTo;

    private Integer rateLimitCount;

    private Integer intervalSec;

    public HttpRateLimitList() {
    }

    public HttpRateLimitList(String method, String path, LocalDateTime limitFrom, LocalDateTime limitTo, Integer rateLimitCount, Integer intervalSec) {
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

    public LocalDateTime getLimitFrom() {
        return limitFrom;
    }

    public void setLimitFrom(String limitFrom) {
        this.limitFrom = LocalDateTime.parse(limitFrom, DatePattern.DATETIME_FORMATTER);
    }

    public LocalDateTime getLimitTo() {
        return limitTo;
    }

    public void setLimitTo(String limitTo) {
        this.limitTo = LocalDateTime.parse(limitTo, DatePattern.DATETIME_FORMATTER);
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
