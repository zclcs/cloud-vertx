package com.zclcs.platform.system.security;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.security.StintProvider;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.platform.system.dao.entity.BlackList;
import com.zclcs.platform.system.dao.entity.RateLimitRule;
import com.zclcs.platform.system.service.BlackListService;
import com.zclcs.platform.system.service.RateLimitRuleService;
import com.zclcs.platform.system.service.impl.BlackListServiceImpl;
import com.zclcs.platform.system.service.impl.RateLimitRuleServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zclcs
 */
public class DefaultStintLogic implements StintProvider {

    private final RateLimitRuleService rateLimitRuleService;
    private final BlackListService blackListService;
    private final List<HttpWhiteList> httpWhiteLists;

    public DefaultStintLogic(JsonObject config, RedisAPI redis, SqlClient sqlClient) {
        this.httpWhiteLists = JsonUtil.readList(config.getString("whiteList"), HttpWhiteList.class);
        this.rateLimitRuleService = new RateLimitRuleServiceImpl(redis, sqlClient);
        this.blackListService = new BlackListServiceImpl(redis, sqlClient);
    }

    @Override
    public Future<HttpWhiteList> getWhiteList(String method, String path) {
        for (HttpWhiteList httpWhiteList : httpWhiteLists) {
            if (method.equalsIgnoreCase(httpWhiteList.getMethod()) && path.equals(httpWhiteList.getPath())) {
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
                        if (limitRule.getRequestMethod().equalsIgnoreCase(method) && limitRule.getRequestUri().equals(path)) {
                            return Future.succeededFuture(limitRule.toHttpRateLimitList());
                        }
                    }
                    return Future.succeededFuture(null);
                });
    }

    @Override
    public Future<List<HttpBlackList>> getBlackList(String method, String path) {
        return blackListService.getEnableBlackListCache().compose(blackList -> {
            List<HttpBlackList> httpBlackLists = new ArrayList<>();
            for (BlackList list : blackList) {
                if (StringsUtil.isBlank(list.getBlackIp()) && method.equalsIgnoreCase(list.getRequestMethod()) && path.equals(list.getRequestUri())) {
                    httpBlackLists.add(list.toHttpBlackList());
                }
            }
            return Future.succeededFuture(httpBlackLists);
        });
    }

    @Override
    public Future<HttpBlackList> getBlackList(String ip, String method, String path) {
        return blackListService.getEnableBlackListCache().compose(blackList -> {
            for (BlackList list : blackList) {
                if (ip.equals(list.getBlackIp()) && method.equalsIgnoreCase(list.getRequestMethod()) && path.equals(list.getRequestUri())) {
                    return Future.succeededFuture(list.toHttpBlackList());
                }
            }
            return Future.succeededFuture(null);
        });
    }

}
