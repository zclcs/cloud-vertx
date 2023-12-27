package com.zclcs.cloud.core.base;

/**
 * @author zclcs
 */
public class HttpResult<T> {

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应体
     */
    private T data;

    /**
     * 时间戳
     */
    private Long time;

    public HttpResult() {
        this.time = System.currentTimeMillis();
    }

    public HttpResult(String msg) {
        this();
        this.msg = msg;
    }

    public HttpResult(T data) {
        this();
        this.data = data;
    }

    public HttpResult(String msg, T data) {
        this(msg);
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public static <T> HttpResult<T> msg(String msg) {
        return new HttpResult<>(msg);
    }

    public static <T> HttpResult<T> data(T data) {
        return new HttpResult<>(data);
    }

    public static <T> HttpResult<T> result(String msg, T data) {
        return new HttpResult<>(msg, data);
    }
}
