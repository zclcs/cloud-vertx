package com.zclcs.cloud.lib.security.logic;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author zclcs
 */
public class RedisTokenLogic implements TokenProvider {

    private final Logger log = LoggerFactory.getLogger(RedisTokenLogic.class);

    private final RedisAPI redis;

    private final LocalCache<String, String> tokenCache;

    public RedisTokenLogic(RedisAPI redis) {
        this.redis = redis;
        this.tokenCache = new LocalCache<>();
    }

    @Override
    public Future<String> generateToken() {
        return Future.succeededFuture(UUID.randomUUID().toString().replace("-", ""));
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
}
