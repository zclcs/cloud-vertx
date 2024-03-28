package com.zclcs.cloud.lib.security.logic;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.common.security.bean.TokenInfo;
import com.zclcs.common.security.constant.LoginDevice;
import com.zclcs.common.security.constant.LoginType;
import com.zclcs.common.security.provider.TokenProvider;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author zclcs
 */
public class RedisTokenLogic implements TokenProvider {

    private final Logger log = LoggerFactory.getLogger(RedisTokenLogic.class);

    private final RedisAPI redis;

    private final LocalCache<String, TokenInfo> tokenCache;

    private final Duration redisTokenExpire = Duration.ofDays(1);

    public RedisTokenLogic(RedisAPI redis) {
        this.redis = redis;
        this.tokenCache = new LocalCache<>(10000, 10, Duration.ofSeconds(5));
    }

    @Override
    public Future<String> generateAndStoreToken(String loginId, LoginType loginType, LoginDevice loginDevice) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String k = String.format(RedisPrefix.TOKEN_PREFIX, token);
        String expireTime = String.valueOf(redisTokenExpire.getSeconds() + new Random().nextLong(100) + 1L);
        TokenInfo tokenInfoValue = new TokenInfo(loginId, loginType, loginDevice);
        return redis.set(Arrays.asList(k, JsonUtil.toJson(tokenInfoValue), "EX", expireTime))
                .compose(rv -> {
                    if (rv != null) {
                        return Future.succeededFuture(token);
                    } else {
                        return Future.failedFuture("token生成失败");
                    }
                });
    }

    @Override
    public Future<TokenInfo> verifyToken(String token) {
        String k = String.format(RedisPrefix.TOKEN_PREFIX, token);
        return tokenCache.getIfPresent(k).compose(v -> {
            if (v != null) {
                return Future.succeededFuture(v);
            } else {
                return redis.get(k).compose(rv -> {
                    String redisToken = rv == null ? "" : rv.toString();
                    TokenInfo value = null;
                    if (StringsUtil.isNotBlank(redisToken)) {
                        value = JsonUtil.readValue(redisToken, TokenInfo.class);
                    } else {
                        return Future.succeededFuture(null);
                    }
                    tokenCache.put(k, value);
                    return Future.succeededFuture(value);
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
