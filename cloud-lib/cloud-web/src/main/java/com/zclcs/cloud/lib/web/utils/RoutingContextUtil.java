package com.zclcs.cloud.lib.web.utils;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.constant.HttpHeaders;
import com.zclcs.common.core.constant.HttpStatus;
import io.vertx.ext.web.RoutingContext;

/**
 * @author zclcs
 */
public class RoutingContextUtil {

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
