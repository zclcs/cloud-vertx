package com.zclcs.platform.system;

import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.WebUtil;
import com.zclcs.cloud.security.SecurityHandler;
import com.zclcs.common.config.starter.ConfigStarter;
import com.zclcs.common.mysql.client.MysqlClientStarter;
import com.zclcs.common.redis.starter.RedisStarter;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.starter.WebStarter;
import com.zclcs.common.web.utils.RoutingContextUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends AbstractVerticle {

    private static final int DEFAULT_PORT = 8201;

    private final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    private WebStarter webStarter;

    private TokenProvider tokenProvider;

    private RedisAPI redis;

    private MysqlClientStarter mysqlClientStarter;

    private Pool mysqlClient;

    private List<HttpWhiteList> whiteList = new ArrayList<>();

    private JsonObject env;

    private RedisStarter redisStarter;

    @Override
    public void start() {
        log.info("isNativeTransportEnabled {}", vertx.isNativeTransportEnabled());
        ConfigStarter configStarter = new ConfigStarter(vertx);
        configStarter.env()
                .andThen(jsonObject -> {
                    env = jsonObject.result();
                    mysqlClientStarter = new MysqlClientStarter(vertx, env);
                    mysqlClient = mysqlClientStarter.connectMysql();
                    redisStarter = new RedisStarter(vertx, env);
                    redisStarter.connectRedis()
                            .onFailure(e -> {
                                log.error("connect redis error {}", e.getMessage(), e);
                                vertx.close();
                            })
                            .andThen(connection -> {
                                redis = RedisAPI.api(redisStarter.getClient());
                                tokenProvider = new RedisTokenLogic(redis);
                            })
                            .andThen(connection -> {
                                webStarter = new WebStarter(vertx, env);
                                webStarter.addRoute("/*", new SecurityHandler(whiteList, tokenProvider));
                                webStarter.addRoute(HttpMethod.GET, "/health", ctx -> RoutingContextUtil.success(ctx, WebUtil.msg("ok")));
                                webStarter.setUpHttpServer(Integer.parseInt(env.getString("PLATFORM_SYSTEM_HTTP_PORT", "8201")),
                                                env.getString("PLATFORM_SYSTEM_HTTP_HOST", "0.0.0.0"),
                                                ctx -> RoutingContextUtil.error(ctx, WebUtil.msg("系统异常")))
                                        .onFailure(e -> {
                                            log.error("start http server error {}", e.getMessage(), e);
                                            vertx.close();
                                        })
                                ;
                            })
                    ;
                })
        ;
    }

    @Override
    public void stop() {
        vertx.executeBlocking((Callable<Void>) () -> {
            if (redisStarter.getClient() != null) {
                redis.close();
                redisStarter.getClient().close();
            }
            return null;
        });
    }

}
