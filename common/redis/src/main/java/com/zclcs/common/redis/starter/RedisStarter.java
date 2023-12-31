package com.zclcs.common.redis.starter;

import com.zclcs.common.core.utils.StringsUtil;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.RedisOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.redis.client.Redis;

import java.util.concurrent.TimeUnit;

/**
 * @author zclcs
 */
public class RedisStarter {

    private final Logger log = LoggerFactory.getLogger(RedisStarter.class);

    private final Vertx vertx;
    private final JsonObject config;
    private String connectionUrl;
    private Redis client;

    public RedisStarter(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    public Completable connectRedis() {
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
        client = Redis.createClient(vertx, options);
        log.info("redis connection url: " + connectionUrl);
        return client.rxConnect().timeout(1, TimeUnit.SECONDS).ignoreElement();
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public Redis getClient() {
        return client;
    }
}
