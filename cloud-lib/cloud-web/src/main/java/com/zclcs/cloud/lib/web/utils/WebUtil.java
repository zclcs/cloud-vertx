package com.zclcs.cloud.lib.web.utils;

import com.zclcs.cloud.core.base.HttpResult;
import com.zclcs.common.config.utils.JsonUtil;

/**
 * @author zclcs
 */
public class WebUtil {

    public static String msg(String msg) {
        return JsonUtil.toJson(HttpResult.msg(msg));
    }


    public static <T> String data(T data) {
        return JsonUtil.toJson(HttpResult.data(data));
    }

    public static <T> String result(String msg, T data) {
        return JsonUtil.toJson(HttpResult.result(msg, data));
    }

}
