package com.zclcs.common.redis.starter;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static io.vertx.core.Future.await;

/**
 * @author zclcs
 */
public class RedisStarter {

    private final Vertx vertx;
    private final JsonObject config;
    private String connectionUrl;
    private RedisAPI redis;

    public RedisStarter(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    public void connectRedis() {
        RedisOptions options = new RedisOptions();
        String redisHost = config.getString("REDIS_HOST", "127.0.0.1");
        String redisPort = config.getString("REDIS_PORT", "6379");
        String redisDatabase = config.getString("REDIS_DATABASE", "0");
        String redisPassword = config.getString("REDIS_PASSWORD", "");
        if (StringsUtil.isBlank(redisPassword)) {
            connectionUrl = String.format("redis://%s:%s/%s", redisHost, redisPort, redisDatabase);
        } else {
            connectionUrl = String.format("redis://:%s@%s:%s/%s", redisPassword, redisHost, redisPort, redisDatabase);
        }
        options.setConnectionString(connectionUrl);
        options.setMaxPoolSize(100);
        options.setMaxPoolWaiting(1000);
        Redis client = Redis.createClient(vertx, options);
        await(client.connect().timeout(1, TimeUnit.SECONDS));
        redis = RedisAPI.api(client);
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public RedisAPI getRedis() {
        return redis;
    }

    public <T, R> AsyncLoadingCache<T, R> createAsyncLoadingCache(Context context, Duration duration, Function<T, Future<R>> future) {
        return Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .recordStats()
                .executor(cmd -> context.runOnContext(v -> cmd.run()))
                .buildAsync((key, exec) -> CompletableFuture.supplyAsync(() ->
                        future.apply(key).toCompletionStage(), exec).thenComposeAsync(Function.identity(), exec));
    }
}
