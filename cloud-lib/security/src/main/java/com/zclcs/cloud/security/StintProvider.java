package com.zclcs.cloud.security;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface StintProvider {

    /**
     * 获取白名单
     *
     * @param method 请求方法
     * @param path   请求路径
     * @return 白名单
     */
    Future<HttpWhiteList> getWhiteList(String method, String path);

    /**
     * 获取限流列表
     *
     * @param method 请求方法
     * @param path   请求路径
     * @return 限流列表
     */
    Future<HttpRateLimitList> getRateLimitList(String method, String path);

    /**
     * 获取黑名单
     *
     * @param method 请求方法
     * @param path   请求路径
     * @return 黑名单
     */
    Future<List<HttpBlackList>> getBlackList(String method, String path);

    /**
     * 获取黑名单
     *
     * @param ip     请求ip
     * @param method 请求方法
     * @param path   请求路径
     * @return 黑名单
     */
    Future<HttpBlackList> getBlackList(String ip, String method, String path);

}
