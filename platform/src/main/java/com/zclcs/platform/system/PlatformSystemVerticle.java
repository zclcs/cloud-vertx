package com.zclcs.platform.system;

import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.WebUtil;
import com.zclcs.cloud.security.SecurityHandler;
import com.zclcs.common.config.starter.ConfigStarter;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.mysql.client.MysqlClientStarter;
import com.zclcs.common.redis.starter.RedisStarter;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.starter.WebStarter;
import com.zclcs.common.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.handler.LoginHandler;
import com.zclcs.platform.system.handler.TestHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.openapi.contract.OpenAPIContract;
import io.vertx.openapi.validation.ValidatorException;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private final List<HttpWhiteList> whiteList = new ArrayList<>();

    private JsonObject env;

    private RedisStarter redisStarter;

    @Override

    public void start() {
        log.info("isNativeTransportEnabled {}", vertx.isNativeTransportEnabled());
        ConfigStarter configStarter = new ConfigStarter(vertx);
        configStarter.setUpMapper();
        configStarter.env()
                .andThen(jsonObject -> {
                    log.info("1");
                    config().getJsonArray("whiteList").stream().forEach(item -> {
                        if (item instanceof JsonObject) {
                            HttpWhiteList httpWhiteList = Json.decodeValue(item.toString(), HttpWhiteList.class);
                            whiteList.add(httpWhiteList);
                        }
                    });
                })
                .andThen(jsonObject -> {
                    log.info("2");
                    env = jsonObject.result();
                    mysqlClientStarter = new MysqlClientStarter(vertx, env);
                    mysqlClient = mysqlClientStarter.connectMysql();
                    mysqlClient.getConnection().onSuccess(SqlClient::close).onFailure(e -> {
                        log.error("connect mysql error {}", e.getMessage(), e);
                        vertx.close();
                    })
                    ;
                })
                .andThen(dc -> {
                    log.info("3");
                    redisStarter = new RedisStarter(vertx, env);
                    redisStarter.connectRedis()
                            .onFailure(e -> {
                                log.error("connect redis error {}", e.getMessage(), e);
                                vertx.close();
                            })
                            .andThen(connection -> {
                                log.info("4");
                                redis = RedisAPI.api(redisStarter.getClient());
                                tokenProvider = new RedisTokenLogic(redis);
                                OpenAPIContract.from(vertx, Objects.requireNonNull(getClass().getClassLoader().getResource("/conf/openapi.json")).getPath())
                                        .onFailure(e -> {
                                            log.error("init openapi error {}", e.getMessage(), e);
                                            vertx.close();
                                        })
                                        .andThen(ar -> {
                                            log.info("5");
                                            webStarter = new WebStarter(vertx, env, ar.result());
                                            this.initRoute(webStarter);
                                            int platformSystemHttpPort = Integer.parseInt(env.getString("PLATFORM_SYSTEM_HTTP_PORT", "8201"));
                                            String platformSystemHttpHost = env.getString("PLATFORM_SYSTEM_HTTP_HOST", "0.0.0.0");
                                            log.info("http server start at {}:{}", platformSystemHttpHost, platformSystemHttpPort);
                                            webStarter.setUpHttpServer(platformSystemHttpPort,
                                                            platformSystemHttpHost,
                                                            ctx -> {
                                                                Throwable failure = ctx.failure();
                                                                if (failure instanceof ValidatorException validatorException) {
                                                                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_BAD_REQUEST, WebUtil.msg(validatorException.getMessage()));
                                                                } else {
                                                                    RoutingContextUtil.error(ctx, WebUtil.msg("系统异常"));
                                                                    log.error("系统异常 {}", failure.getMessage(), failure);
                                                                }
                                                            })
                                                    .onFailure(e -> {
                                                        log.error("start http server error {}", e.getMessage(), e);
                                                        vertx.close();
                                                    })
                                            ;
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

    private void initRoute(WebStarter webStarter) {
        webStarter.addRoute("/*", new SecurityHandler(whiteList, tokenProvider));
        webStarter.addOpenApiRoute("loginTokenByUsername", new LoginHandler(mysqlClient));
        webStarter.addRoute(HttpMethod.GET, "/health", ctx -> RoutingContextUtil.success(ctx, WebUtil.msg("ok")));
        webStarter.addRoute(HttpMethod.GET, "/test", new TestHandler(mysqlClient));
    }

}
