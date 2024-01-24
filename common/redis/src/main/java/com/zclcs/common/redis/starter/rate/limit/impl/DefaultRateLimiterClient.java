package com.zclcs.common.redis.starter.rate.limit.impl;

import com.zclcs.common.redis.starter.rate.limit.RateLimiterClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.redis.client.RedisAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zclcs
 */
public class DefaultRateLimiterClient implements RateLimiterClient {

    private final Logger log = LoggerFactory.getLogger(getClass());

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
    private final Map<String, String> luaScripts = new HashMap<>();

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
        String luaFileName = "rate_limiter.lua";
        if (luaScripts.get(luaFileName) == null) {
            return vertx.fileSystem().readFile("scripts/" + luaFileName).compose(file ->
                    redis.script(List.of("load", file.toString())).compose(sha -> {
                        String shaString = sha.toString();
                        luaScripts.put(luaFileName, shaString);
                        return evalsha(luaScripts.get(luaFileName), redisKeyBuilder, max, ttlMillis);
                    }));
        } else {
            return evalsha(luaScripts.get(luaFileName), redisKeyBuilder, max, ttlMillis);
        }
    }

    private Future<Boolean> evalsha(String sha, String key, long max, long ttl) {
        return redis.evalsha(List.of(sha, "1", key, String.valueOf(max), String.valueOf(ttl)))
                .compose(response -> Future.succeededFuture(response != null && response.toInteger() != FAIL_CODE));
    }
}
