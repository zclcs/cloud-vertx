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
import com.zclcs.platform.system.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.openapi.contract.OpenAPIContract;
import io.vertx.openapi.validation.ValidatorException;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    private WebStarter webStarter;

    private TokenProvider tokenProvider;

    private RedisAPI redis;

    private MysqlClientStarter mysqlClientStarter;

    private Pool mysqlClient;

    private final List<HttpWhiteList> whiteList = new ArrayList<>();

    private JsonObject env;

    private RedisStarter redisStarter;

    private int port;

    private String host;

    private final Set<Record> records = new HashSet<>();

    @Override
    public void start() {
        log.info("isNativeTransportEnabled {}", vertx.isNativeTransportEnabled());
        ConfigStarter configStarter = new ConfigStarter(vertx);
        configStarter.setUpMapper();
        initWhiteList();
        configStarter.env()
                .andThen(jsonObject -> {
                    env = jsonObject.result();
                    port = Integer.parseInt(env.getString("PLATFORM_SYSTEM_HTTP_PORT", "8201"));
                    host = env.getString("PLATFORM_SYSTEM_HTTP_HOST", "0.0.0.0");
                    mysqlClientStarter = new MysqlClientStarter(vertx, env);
                    mysqlClient = mysqlClientStarter.createMysqlPool();
                    mysqlClient.getConnection()
                            .onSuccess(mysqlConnection -> {
                                log.info("test connect mysql success");
                                mysqlConnection.close();
                            })
                            .onFailure(e -> {
                                log.error("connect mysql error {}", e.getMessage(), e);
                                vertx.close();
                            });
                    redisStarter = new RedisStarter(vertx, env);
                    redisStarter.connectRedis()
                            .onSuccess(redisConnection -> {
                                log.info("test connect redis success");
                                redisConnection.close();
                            })
                            .onFailure(e -> {
                                log.error("connect redis error {}", e.getMessage(), e);
                                vertx.close();
                            })
                            .andThen(connection -> {
                                redis = RedisAPI.api(redisStarter.getClient());
                                tokenProvider = new RedisTokenLogic(redis);
                            })
                            .andThen(redisConnection ->
                                    getContractFilePath()
                                            .andThen(file -> {
                                                JsonObject contractJson = file.result().toJsonObject();
                                                OpenAPIContract.from(vertx, contractJson)
                                                        .onFailure(e -> {
                                                            log.error("init openapi error {}", e.getMessage(), e);
                                                            vertx.close();
                                                        })
                                                        .andThen(ar -> {
                                                            webStarter = new WebStarter(vertx, env, ar.result());
                                                            this.initRoute(webStarter);
                                                            webStarter.setUpHttpServer(port,
                                                                            host,
                                                                            ctx -> {
                                                                                Throwable failure = ctx.failure();
                                                                                if (failure instanceof ValidatorException validatorException) {
                                                                                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_BAD_REQUEST, WebUtil.msg(validatorException.getMessage()));
                                                                                } else {
                                                                                    RoutingContextUtil.error(ctx, WebUtil.msg("系统异常"));
                                                                                    log.error("系统异常 {}", failure.getMessage(), failure);
                                                                                }
                                                                            })
                                                                    .onSuccess(server -> log.info("http server start at {}:{}", host, port))
                                                                    .onFailure(e -> {
                                                                        log.error("start http server error {}", e.getMessage(), e);
                                                                        vertx.close();
                                                                    })
                                                            ;
                                                        })
                                                ;
                                            }))
                    ;
                })
        ;
    }

    @Override
    public void stop(Promise<Void> promise) {
        vertx.executeBlocking((Callable<Object>) () -> {
            if (redisStarter.getClient() != null) {
                redisStarter.getClient().close();
            }
            if (mysqlClient != null) {
                mysqlClient.close();
            }
            return null;
        }).onComplete(c -> promise.complete(), e -> {
            e.printStackTrace();
            promise.fail(e);
        });
    }

    private void initRoute(WebStarter webStarter) {
        webStarter.addRoute("/*", new SecurityHandler(whiteList, tokenProvider));
        webStarter.addOpenApiRoute("loginTokenByUsername", new LoginHandler(new UserServiceImpl(mysqlClient, redis), tokenProvider));
        webStarter.addOpenApiRoute("test", new TestHandler(new UserServiceImpl(mysqlClient, redis)));
        webStarter.addRoute(HttpMethod.GET, "/health", ctx -> RoutingContextUtil.success(ctx, WebUtil.msg("ok")));
    }

    private Future<Buffer> getContractFilePath() {
        return vertx.fileSystem().readFile("conf/openapi.json");
    }

    private void initWhiteList() {
        config().getJsonArray("whiteList").stream().forEach(item -> {
            if (item instanceof JsonObject) {
                HttpWhiteList httpWhiteList = Json.decodeValue(item.toString(), HttpWhiteList.class);
                whiteList.add(httpWhiteList);
            }
        });
    }

}
