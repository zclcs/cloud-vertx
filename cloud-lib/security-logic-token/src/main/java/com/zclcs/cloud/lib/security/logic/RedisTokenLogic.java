package com.zclcs.cloud.lib.security.logic;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.local.cache.AsyncLoadingCacheUtil;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class RedisTokenLogic implements TokenProvider {

    private final Logger log = LoggerFactory.getLogger(RedisTokenLogic.class);

    private final RedisAPI redis;

    private final AsyncLoadingCache<String, String> tokenLocalCache;

    public RedisTokenLogic(RedisAPI redis, Context context) {
        this.redis = redis;
        this.tokenLocalCache = AsyncLoadingCacheUtil.buildAsyncLoadingCache(context, k -> redis.get(k).map(Response::toString));
    }

    @Override
    public Future<String> generateToken() {
        return null;
    }

    @Override
    public void verifyToken(String token, Handler<AsyncResult<String>> handler) {
        String k = String.format(RedisPrefix.TOKEN_PREFIX, token);
        Future.fromCompletionStage(tokenLocalCache.get(k)).onComplete(handler);
    }
}
