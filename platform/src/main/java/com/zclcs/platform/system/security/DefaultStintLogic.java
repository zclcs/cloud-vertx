package com.zclcs.platform.system.security;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.security.StintProvider;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.platform.system.dao.entity.RateLimitRule;
import com.zclcs.platform.system.service.BlackListService;
import com.zclcs.platform.system.service.RateLimitRuleService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.RedisAPI;

import java.time.Duration;
import java.util.List;

/**
 * @author zclcs
 */
public class DefaultStintLogic implements StintProvider {

    private final RedisAPI redis;
    private final RateLimitRuleService rateLimitRuleService;
    private final BlackListService blackListService;
    private final List<HttpWhiteList> httpWhiteLists;

    private final Duration redisExpire = Duration.ofDays(1);

    public DefaultStintLogic(JsonObject config, RedisAPI redis, RateLimitRuleService rateLimitRuleService, BlackListService blackListService) {
        this.redis = redis;
        this.httpWhiteLists = JsonUtil.readList(config.getString("whiteList"), HttpWhiteList.class);
        this.rateLimitRuleService = rateLimitRuleService;
        this.blackListService = blackListService;
    }

    @Override
    public Future<HttpWhiteList> getWhiteList(String method, String path) {
        for (HttpWhiteList httpWhiteList : httpWhiteLists) {
            if (httpWhiteList.getMethod().equals(method) && httpWhiteList.getPath().equals(path)) {
                return Future.succeededFuture(httpWhiteList);
            }
        }
        return Future.succeededFuture(null);
    }

    @Override
    public Future<HttpRateLimitList> getRateLimitList(String method, String path) {
        return rateLimitRuleService.getRateEnableLimitRuleCache()
                .compose(rateLimitRule -> {
                    for (RateLimitRule limitRule : rateLimitRule) {
                        if (limitRule.getRequestMethod().equals(method) && limitRule.getRequestUri().equals(path)) {
                            return Future.succeededFuture(limitRule.toHttpRateLimitList());
                        }
                    }
                    return Future.succeededFuture(null);
                });
    }

    @Override
    public Future<List<HttpBlackList>> getBlackList(String method, String path) {
        return null;
    }

    @Override
    public Future<HttpBlackList> getBlackList(String ip, String method, String path) {
        return null;
    }

}
