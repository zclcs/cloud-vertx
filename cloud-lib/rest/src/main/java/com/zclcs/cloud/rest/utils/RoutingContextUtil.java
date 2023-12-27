package com.zclcs.cloud.rest.utils;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.common.core.constant.HttpHeaders;
import com.zclcs.common.core.constant.HttpStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * @author zclcs
 */
public class RoutingContextUtil {

    public static <T> void success(RoutingContext ctx, HttpResult<T> result) {
        HttpServerResponse response = ctx.response();
        response.setStatusCode(HttpStatus.HTTP_OK);
        response.putHeader(HttpHeaders.APPLICATION_JSON_UTF8.name(), HttpHeaders.APPLICATION_JSON_UTF8.value());
        response.end(Json.encode(result));
    }

    public static <T> void error(RoutingContext ctx, HttpResult<T> result) {
        ctx.response().setStatusCode(HttpStatus.HTTP_INTERNAL_ERROR).end(Json.encode(result));
    }

    public static <T> void error(RoutingContext ctx, int httpStatus, HttpResult<T> result) {
        ctx.response().setStatusCode(httpStatus).end(Json.encode(result));
    }
}
