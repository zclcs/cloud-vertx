package com.zclcs.cloud.lib.security.logic;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class RedisTokenLogic implements TokenProvider {

    private final RedisAPI redis;

    private final LocalCache<String, String> tokenLocalCache = new LocalCache<>();

    public RedisTokenLogic(RedisAPI redis) {
        this.redis = redis;
    }

    @Override

    public Future<String> generateToken() {
        return null;
    }

    @Override
    public Future<Boolean> verifyToken(String token) {
        String k = String.format(RedisPrefix.TOKEN_PREFIX, token);
        String localCacheResult = await(tokenLocalCache.getIfPresent(k));
        if (localCacheResult == null) {
            Response redisResult = await(redis.get(k));
            if (redisResult != null) {
                await(tokenLocalCache.put(k, redisResult.toString()));
                return Future.succeededFuture(true);
            }
            return Future.succeededFuture(false);
        }
        return Future.succeededFuture(true);
    }
}
