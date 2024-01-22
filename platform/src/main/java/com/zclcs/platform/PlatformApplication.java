package com.zclcs.platform;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.common.config.starter.ConfigStarter;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.mysql.client.MysqlClientStarter;
import com.zclcs.common.redis.starter.RedisStarter;
import com.zclcs.platform.system.PlatformSystemVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zclcs
 */
public class PlatformApplication extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(PlatformApplication.class);

    private final List<HttpWhiteList> whiteList = new ArrayList<>();

    private final List<HttpRateLimitList> rateLimitList = new ArrayList<>();

    private final List<HttpBlackList> blackList = new ArrayList<>();

    private JsonObject env;

    @Override
    public void start() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        ConfigStarter configStarter = new ConfigStarter(vertx);
        configStarter.setUpMapper();
        configStarter.env()
                .andThen(jsonObject -> {
                    initWhiteList().onComplete(whiteList::addAll, e -> {
                        log.error("init white list error {}", e.getMessage(), e);
                        vertx.close();
                    });
                })
                .andThen(jsonObject -> {
                    env = jsonObject.result();
                    MysqlClientStarter mysqlClientStarter = new MysqlClientStarter(vertx, env);
                    Pool pool = mysqlClientStarter.createPool();
                    SqlClient sqlClient = mysqlClientStarter.createSqlClient();
                    pool.getConnection()
                            .onComplete(mysqlConnection -> {
                                log.info("test connect mysql success");
                            }, e -> {
                                log.error("connect mysql error {}", e.getMessage(), e);
                                vertx.close();
                            })
                            .andThen(ar -> {
                                mysqlClientStarter.initDb(ar.result())
                                        .onComplete(initDb -> {
                                            log.info("init mysql db success cost {} ms", (System.currentTimeMillis() - currentTimeMillis));
                                        }, e -> {
                                            log.error("init mysql db error {}", e.getMessage(), e);
                                            vertx.close();
                                        })
                                ;
                                RedisStarter redisStarter = new RedisStarter(vertx, env);
                                redisStarter.connectRedis()
                                        .onComplete(redisConn -> {
                                            log.info("test connect redis success");
                                            redisConn.close();
                                        }, e -> {
                                            log.error("connect redis error {}", e.getMessage(), e);
                                            vertx.close();
                                        })
                                        .andThen(redisConn -> {
                                            int platformSystemInstances = Integer.parseInt(env.getString("PLATFORM_SYSTEM_INSTANCES", "1"));
                                            vertx.deployVerticle(() -> {
                                                                int platformSystemHttpPort = Integer.parseInt(env.getString("PLATFORM_SYSTEM_HTTP_PORT", "8201"));
                                                                String platformSystemHttpHost = env.getString("PLATFORM_SYSTEM_HTTP_HOST", "0.0.0.0");
                                                                RedisAPI redis = RedisAPI.api(redisStarter.getClient());
                                                                return new PlatformSystemVerticle(env, sqlClient, redis, whiteList, rateLimitList, blackList, platformSystemHttpPort, platformSystemHttpHost);
                                                            },
                                                            new DeploymentOptions().setConfig(config()).setInstances(platformSystemInstances))
                                                    .onComplete(s -> {
                                                        log.info("PlatformSystem deploy success cost {} ms", (System.currentTimeMillis() - currentTimeMillis));
                                                    }, e -> {
                                                        log.error("deploy verticle error {}", e.getMessage(), e);
                                                    });
                                        })
                                ;
                            })
                    ;
                });
    }

    private Future<List<HttpWhiteList>> initWhiteList() {
        String string = config().getString("whiteList");
        if (string == null) {
            return Future.succeededFuture();
        }
        try {
            List<HttpWhiteList> httpWhiteLists = JsonUtil.readList(string, HttpWhiteList.class);
            return Future.succeededFuture(httpWhiteLists);
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }

    private Future<Buffer> getContractFilePath() {
        return vertx.fileSystem().readFile("conf/openapi.yaml");
    }

}
