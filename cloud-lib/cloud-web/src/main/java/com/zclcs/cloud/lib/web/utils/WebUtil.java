package com.zclcs.cloud.lib.web.utils;

import com.zclcs.cloud.core.base.HttpResult;
import io.vertx.core.json.Json;

/**
 * @author zclcs
 */
public class WebUtil {

    public static String msg(String msg) {
        return Json.encode(HttpResult.msg(msg));
    }


    public static <T> String data(T data) {
        return Json.encode(HttpResult.data(data));
    }

    public static <T> String result(String msg, T data) {
        return Json.encode(HttpResult.result(msg, data));
    }

}
