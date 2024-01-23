package com.zclcs.cloud.core.exception;

import com.zclcs.common.core.constant.HttpStatus;

/**
 * @author zclcs
 */
public class SecurityException extends RuntimeException {

    private int httpStatus;

    private String msg;

    public SecurityException() {
        this(HttpStatus.HTTP_INTERNAL_ERROR, "系统异常");
    }

    public SecurityException(int httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
        this.msg = msg;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMsg() {
        return msg;
    }

}
