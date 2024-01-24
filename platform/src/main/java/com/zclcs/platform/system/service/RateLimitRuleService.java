package com.zclcs.platform.system.service;

import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.platform.system.dao.entity.RateLimitRule;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface RateLimitRuleService {

    /**
     * 获取限流规则
     *
     * @return Future
     */
    Future<List<RateLimitRule>> getRateEnableLimitRule();


    /**
     * 获取限流规则缓存
     *
     * @return 限流规则缓存
     */
    Future<List<HttpRateLimitList>> getRateEnableHttpRateLimitListCache();

}
