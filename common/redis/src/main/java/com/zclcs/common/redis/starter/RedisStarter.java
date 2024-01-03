package com.zclcs.common.redis.starter;

import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.RedisOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author zclcs
 */
public class RedisStarter {

    private final Logger log = LoggerFactory.getLogger(RedisStarter.class);

    private final Vertx vertx;
    private final JsonObject env;
    private final JsonObject config;
    private String connectionUrl;
    private Redis client;

    /**
     * @param vertx  Vert.x Core API.
     * @param env    环境变量
     * @param config 配置文件
     */
    public RedisStarter(Vertx vertx, JsonObject env) {
        this.vertx = vertx;
        this.env = env;
        this.config = vertx.getOrCreateContext().config();
    }

    public Future<RedisConnection> connectRedis() {
        RedisOptions options = new RedisOptions();
        String redisHost = StringsUtil.chooseOneIsNotBlank(env.getString("REDIS_HOST"), config.getString("redis.host"), "127.0.0.1");
        String redisPort = StringsUtil.chooseOneIsNotBlank(env.getString("REDIS_PORT"), config.getString("redis.port"), "6379");
        String redisDatabase = StringsUtil.chooseOneIsNotBlank(env.getString("REDIS_DATABASE"), config.getString("redis.database"), "0");
        String redisPassword = StringsUtil.chooseOneIsNotBlank(env.getString("REDIS_PASSWORD"), config.getString("redis.password"), "");
        String redisMaxPoolSize = StringsUtil.chooseOneIsNotBlank(env.getString("REDIS_MAX_POOL_SIZE"), config.getString("redis.maxPoolSize"), "100");
        String redisMaxPoolWaiting = StringsUtil.chooseOneIsNotBlank(env.getString("REDIS_MAX_POOL_WAITING"), config.getString("redis.maxPoolWaiting"), "1000");
        if (StringsUtil.isBlank(redisPassword)) {
            connectionUrl = String.format("redis://%s:%s/%s", redisHost, redisPort, redisDatabase);
        } else {
            connectionUrl = String.format("redis://:%s@%s:%s/%s", redisPassword, redisHost, redisPort, redisDatabase);
        }
        options.setConnectionString(connectionUrl);
        options.setMaxPoolSize(Integer.parseInt(redisMaxPoolSize));
        options.setMaxPoolWaiting(Integer.parseInt(redisMaxPoolWaiting));
        client = Redis.createClient(vertx, options);
        log.info("redis connection url {}", connectionUrl);
        return client.connect().timeout(1, TimeUnit.SECONDS);
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public Redis getClient() {
        return client;
    }
}
