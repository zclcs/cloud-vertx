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
     * @return 白名单
     */
    Future<List<HttpWhiteList>> getWhiteList();

    /**
     * 获取限流列表
     *
     * @return 限流列表
     */
    Future<List<HttpRateLimitList>> getRateLimitList();

    /**
     * 获取黑名单
     *
     * @return 黑名单
     */
    Future<List<HttpBlackList>> getBlackList();

}
