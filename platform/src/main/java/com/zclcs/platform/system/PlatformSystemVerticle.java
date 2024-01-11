package com.zclcs.platform.system;

import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.SecurityHandler;
import com.zclcs.common.config.starter.ConfigStarter;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.mysql.client.MysqlClientStarter;
import com.zclcs.common.redis.starter.RedisStarter;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.starter.WebStarter;
import com.zclcs.platform.system.handler.*;
import com.zclcs.platform.system.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
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
        long currentTimeMillis = System.currentTimeMillis();
        log.info("isNativeTransportEnabled {}", vertx.isNativeTransportEnabled());
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
                                            .onFailure(e -> {
                                                log.error("get contract file error {}", e.getMessage(), e);
                                                vertx.close();
                                            })
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
                                                                                    RoutingContextUtil.error(ctx, HttpStatus.HTTP_BAD_REQUEST, validatorException.getMessage());
                                                                                } else {
                                                                                    RoutingContextUtil.error(ctx, "系统异常");
                                                                                    log.error("系统异常 {}", failure.getMessage(), failure);
                                                                                }
                                                                            })
                                                                    .onSuccess(server -> {
                                                                        log.info("http server start at {}:{}", host, port);
                                                                        log.info("platformSystemVerticle start cost {} ms", System.currentTimeMillis() - currentTimeMillis);
                                                                    })
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
        vertx.executeBlocking(() -> {
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
        webStarter.addOpenApiRoute("VerifyCodeHandler", new VerifyCodeHandler(redis));
        UserServiceImpl userService = new UserServiceImpl(mysqlClient, redis);
        webStarter.addOpenApiRoute("LoginByUsernameHandler", new LoginByUsernameHandler(redis, config(), userService, tokenProvider));
        webStarter.addOpenApiRoute("UserPermissionsHandler", new UserPermissionsHandler(userService));
        webStarter.addOpenApiRoute("UserRoutersHandler", new UserRoutersHandler(userService));
        webStarter.addOpenApiRoute("test", new TestHandler(userService));
        webStarter.addRoute(HttpMethod.GET, "/health", ctx -> RoutingContextUtil.success(ctx, "ok"));
        webStarter.errorHandler(404, (ctx) -> RoutingContextUtil.error(ctx, "接口未找到"));
    }

    private Future<Buffer> getContractFilePath() {
        return vertx.fileSystem().readFile("conf/openapi.json");
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

}
