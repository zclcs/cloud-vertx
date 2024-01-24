package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.entity.RateLimitRule;
import com.zclcs.platform.system.dao.entity.RateLimitRuleRowMapper;
import com.zclcs.platform.system.service.RateLimitRuleService;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RateLimitRuleServiceImpl implements RateLimitRuleService {

    private final RedisAPI redis;

    private final Duration redisExpire = Duration.ofDays(1);

    private final SqlClient sqlClient;

    public RateLimitRuleServiceImpl(RedisAPI redis, SqlClient sqlClient) {
        this.redis = redis;
        this.sqlClient = sqlClient;
    }

    private final LocalCache<String, List<RateLimitRule>> rateLimitRuleListCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    @Override
    public Future<List<RateLimitRule>> getRateEnableLimitRule() {
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT 
                             `rate_limit_rule_id`, 
                             `request_uri`, 
                             `request_method`, 
                             `limit_from`, 
                             `limit_to`, 
                             `rate_limit_count`, 
                             `interval_sec`, 
                             `rule_status`
                        FROM system_rate_limit_rule 
                            where rule_status = "1"
                        """)
                .mapTo(RateLimitRuleRowMapper.INSTANCE)
                .execute(Collections.emptyMap())
                .flatMap(rows -> {
                    List<RateLimitRule> rateLimitRules = new ArrayList<>();
                    rows.forEach(rateLimitRules::add);
                    return Future.succeededFuture(rateLimitRules);
                });
    }

    @Override
    public Future<List<RateLimitRule>> getRateEnableLimitRuleCache() {
        return rateLimitRuleListCache.getIfPresent(RedisPrefix.RATE_LIMIT_LIST_PREFIX).compose(blackLists -> {
            if (blackLists != null) {
                return Future.succeededFuture(blackLists);
            } else {
                return redis.get(RedisPrefix.RATE_LIMIT_LIST_PREFIX).compose(response -> {
                    if (response != null) {
                        List<RateLimitRule> redisValue = JsonUtil.readList(response.toString(), RateLimitRule.class);
                        rateLimitRuleListCache.put(RedisPrefix.RATE_LIMIT_LIST_PREFIX, redisValue);
                        return Future.succeededFuture(redisValue);
                    } else {
                        return getRateEnableLimitRule().compose(dv -> {
                            String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                            if (!dv.isEmpty()) {
                                redis.set(List.of(RedisPrefix.RATE_LIMIT_LIST_PREFIX, JsonUtil.toJson(dv), "EX", expireTime));
                                return Future.succeededFuture(dv);
                            } else {
                                return null;
                            }
                        });
                    }
                });
            }
        });
    }


}