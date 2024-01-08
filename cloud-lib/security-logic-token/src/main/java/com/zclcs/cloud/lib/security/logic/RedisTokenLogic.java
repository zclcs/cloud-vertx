package com.zclcs.cloud.lib.security.logic;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * @author zclcs
 */
public class RedisTokenLogic implements TokenProvider {

    private final Logger log = LoggerFactory.getLogger(RedisTokenLogic.class);

    private final RedisAPI redis;

    private final LocalCache<String, String> tokenCache;

    private final Duration redisTokenExpire = Duration.ofDays(1);

    public RedisTokenLogic(RedisAPI redis) {
        this.redis = redis;
        this.tokenCache = new LocalCache<>(10000, 10, Duration.ofSeconds(5));
    }

    @Override
    public Future<String> generateAndStoreToken(String loginId, String loginType) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String k = String.format(RedisPrefix.TOKEN_PREFIX, token);
        return redis.set(List.of(k, loginId, "EX", String.valueOf(redisTokenExpire.getSeconds() + Math.random() * 100 + 1))).compose(rv -> {
            if (rv != null) {
                return Future.succeededFuture(token);
            } else {
                return Future.failedFuture("token生成失败");
            }
        });
    }

    @Override
    public Future<String> verifyToken(String token) {
        String k = String.format(RedisPrefix.TOKEN_PREFIX, token);
        return tokenCache.getIfPresent(k).compose(v -> {
            if (v != null) {
                return Future.succeededFuture(v);
            } else {
                return redis.get(k).map(Response::toString).compose(rv -> {
                    if (rv != null) {
                        tokenCache.put(k, rv);
                        return Future.succeededFuture(rv);
                    } else {
                        return Future.succeededFuture(null);
                    }
                });
            }
        });
    }

    public Future<Void> deleteToken(String token) {
        return redis.del(List.of(String.format(RedisPrefix.TOKEN_PREFIX, token))).compose(rv -> {
            if (rv != null) {
                return Future.succeededFuture(null);
            } else {
                return Future.failedFuture("token删除失败");
            }
        });
    }

    @Override
    public Duration getRedisTokenExpire() {
        return redisTokenExpire;
    }
}
