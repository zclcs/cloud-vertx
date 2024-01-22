package com.zclcs.common.redis.starter.rate.limit.impl;

import com.zclcs.common.redis.starter.rate.limit.RateLimiterClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.redis.client.RedisAPI;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DefaultRateLimiterClient implements RateLimiterClient {

    /**
     * redis 限流 key 前缀
     */
    private static final String REDIS_KEY_PREFIX = "limiter:";

    /**
     * 失败的默认返回值
     */
    private static final long FAIL_CODE = 0;

    private final Vertx vertx;
    private final RedisAPI redis;

    public DefaultRateLimiterClient(Vertx vertx, RedisAPI redis) {
        this.vertx = vertx;
        this.redis = redis;
    }

    @Override
    public Future<Boolean> isAllowed(String key, long max, long ttl, TimeUnit timeUnit) {
        // redis key
        String redisKeyBuilder = REDIS_KEY_PREFIX + key;
        // 转为毫秒
        long ttlMillis = timeUnit.toMillis(ttl);
        return vertx.fileSystem().readFile("scripts/rate_limit.lua").compose(file -> {
            String string = file.toString();
            if (string == null) {
                return Future.failedFuture("lua file not found");
            }
            return redis.eval(List.of(string, redisKeyBuilder, String.valueOf(max), String.valueOf(ttlMillis)))
                    .compose(response -> Future.succeededFuture(response != null && response.toInteger() != FAIL_CODE));
        });
    }
}
