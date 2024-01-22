package com.zclcs.cloud.lib.web.utils;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.constant.HttpHeaders;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.core.constant.StringPool;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * @author zclcs
 */
public class RoutingContextUtil {

    private static final String UNKNOWN = "unknown";

    /**
     * 获取请求IP
     *
     * @param ctx 请求上下文
     * @return String IP
     */
    public static String getHttpRequestIpAddress(RoutingContext ctx) {
        HttpServerRequest request = ctx.request();
        MultiMap headers = ctx.request().headers();
        String ip = headers.get("x-forwarded-for");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.get("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.get("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.get("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.get("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = headers.get("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.localAddress().hostAddress();
        }
        if (ip.contains(StringPool.COMMA)) {
            ip = ip.split(StringPool.COMMA)[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static void success(RoutingContext ctx, String msg) {
        ctx.response()
                .setStatusCode(HttpStatus.HTTP_OK)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(msg(msg));
    }

    public static <T> void success(RoutingContext ctx, T data) {
        ctx.response()
                .setStatusCode(HttpStatus.HTTP_OK)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(data(data));
    }

    public static <T> void success(RoutingContext ctx, String msg, T data) {
        ctx.response()
                .setStatusCode(HttpStatus.HTTP_OK)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(result(msg, data));
    }

    public static void error(RoutingContext ctx, String msg) {
        ctx.response()
                .setStatusCode(HttpStatus.HTTP_INTERNAL_ERROR)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(msg(msg));
    }

    public static void error(RoutingContext ctx, int httpStatus, String msg) {
        ctx.response()
                .setStatusCode(httpStatus)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(msg(msg));
    }

    private static String msg(String msg) {
        return JsonUtil.toJson(HttpResult.msg(msg));
    }


    private static <T> String data(T data) {
        return JsonUtil.toJson(HttpResult.data(data));
    }

    private static <T> String result(String msg, T data) {
        return JsonUtil.toJson(HttpResult.result(msg, data));
    }
}
