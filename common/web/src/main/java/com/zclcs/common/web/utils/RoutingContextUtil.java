package com.zclcs.common.web.utils;

import com.zclcs.common.core.constant.HttpHeaders;
import com.zclcs.common.core.constant.HttpStatus;
import io.vertx.ext.web.RoutingContext;

/**
 * @author zclcs
 */
public class RoutingContextUtil {

    public static <T> void success(RoutingContext ctx, String result) {
        ctx.response()
                .setStatusCode(HttpStatus.HTTP_OK)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(result);
    }

    public static <T> void error(RoutingContext ctx, String result) {
        ctx.response()
                .setStatusCode(HttpStatus.HTTP_INTERNAL_ERROR)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(result);
    }

    public static <T> void error(RoutingContext ctx, int httpStatus, String result) {
        ctx.response()
                .setStatusCode(httpStatus)
                .putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value())
                .end(result);
    }
}
