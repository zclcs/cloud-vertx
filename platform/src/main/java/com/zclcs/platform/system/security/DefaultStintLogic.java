package com.zclcs.platform.system.security;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.security.StintProvider;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.platform.system.service.BlackListService;
import com.zclcs.platform.system.service.RateLimitRuleService;
import com.zclcs.platform.system.service.impl.BlackListServiceImpl;
import com.zclcs.platform.system.service.impl.RateLimitRuleServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;

import java.util.List;

/**
 * @author zclcs
 */
public class DefaultStintLogic implements StintProvider {

    private final RateLimitRuleService rateLimitRuleService;
    private final BlackListService blackListService;
    private final List<HttpWhiteList> httpWhiteLists;

    public DefaultStintLogic(JsonObject config, RedisAPI redis, Pool pool) {
        this.httpWhiteLists = JsonUtil.readList(config.getString("whiteList"), HttpWhiteList.class);
        this.rateLimitRuleService = new RateLimitRuleServiceImpl(redis, pool);
        this.blackListService = new BlackListServiceImpl(redis, pool);
    }

    @Override
    public Future<List<HttpWhiteList>> getWhiteList() {
        return Future.succeededFuture(httpWhiteLists);
    }

    @Override
    public Future<List<HttpRateLimitList>> getRateLimitList() {
        return rateLimitRuleService.getRateEnableHttpRateLimitListCache();
    }

    @Override
    public Future<List<HttpBlackList>> getBlackList() {
        return blackListService.getEnableHttpBlackListCache();
    }

}
