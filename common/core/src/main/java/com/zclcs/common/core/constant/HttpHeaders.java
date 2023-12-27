package com.zclcs.common.core.constant;

import com.zclcs.common.core.record.HttpHeader;

/**
 * @author zclcs
 */
public class HttpHeaders {

    public static final HttpHeader APPLICATION_JSON = new HttpHeader("Content-Type", "application/json");

    public static final HttpHeader APPLICATION_JSON_UTF8 = new HttpHeader("Content-Type", "application/json;charset=utf-8");

}
