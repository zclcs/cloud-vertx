package com.zclcs.platform.system;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.bean.HttpRateLimitList;
import com.zclcs.cloud.core.bean.HttpWhiteList;
import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.GlobalHandler;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.redis.starter.rate.limit.RateLimiterClient;
import com.zclcs.common.redis.starter.rate.limit.impl.DefaultRateLimiterClient;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.starter.WebStarter;
import com.zclcs.platform.system.handler.*;
import com.zclcs.platform.system.security.HasPermissionLogic;
import com.zclcs.platform.system.service.DeptService;
import com.zclcs.platform.system.service.RoleService;
import com.zclcs.platform.system.service.UserService;
import com.zclcs.platform.system.service.impl.DeptServiceImpl;
import com.zclcs.platform.system.service.impl.RoleServiceImpl;
import com.zclcs.platform.system.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.openapi.contract.OpenAPIContract;
import io.vertx.openapi.validation.ValidatorException;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    private WebStarter webStarter;

    private final TokenProvider tokenProvider;

    private final RateLimiterClient rateLimiterClient;

    private final RedisAPI redis;

    private final SqlClient sqlClient;

    private final List<HttpWhiteList> whiteList;

    private final List<HttpRateLimitList> rateLimitList;

    private final List<HttpBlackList> blackList;

    private final JsonObject env;

    private final int httpPort;

    private final String httpHost;

    public PlatformSystemVerticle(JsonObject env, SqlClient sqlClient, RedisAPI redis, List<HttpWhiteList> whiteList, List<HttpRateLimitList> rateLimitList, List<HttpBlackList> blackList, int httpPort, String httpHost) {
        this.env = env;
        this.sqlClient = sqlClient;
        this.redis = redis;
        this.whiteList = whiteList;
        this.rateLimitList = rateLimitList;
        this.blackList = blackList;
        this.httpPort = httpPort;
        this.httpHost = httpHost;
        this.tokenProvider = new RedisTokenLogic(redis);
        this.rateLimiterClient = new DefaultRateLimiterClient(vertx, redis);
    }

    @Override
    public void start() {
        OpenAPIContract.from(vertx, "conf/openapi.yaml")
                .onFailure(e -> {
                    log.error("init openapi error {}", e.getMessage(), e);
                    vertx.close();
                })
                .andThen(ar -> {
                    webStarter = new WebStarter(vertx, env, ar.result());
                    this.initRoute(webStarter);
                    webStarter.setUpHttpServer(
                                    httpPort,
                                    httpHost,
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
                                log.info("http server start at {}:{}", httpHost, httpPort);
                            })
                            .onFailure(e -> {
                                log.error("start http server error {}", e.getMessage(), e);
                                vertx.close();
                            })
                    ;
                });
    }

    private void initRoute(WebStarter webStarter) {
        webStarter.addRoute("/*", new GlobalHandler(whiteList, rateLimitList, blackList, tokenProvider, rateLimiterClient));
        webStarter.addOpenApiRoute("VerifyCodeHandler", new VerifyCodeHandler(redis));
        RoleService roleService = new RoleServiceImpl(sqlClient);
        DeptService deptService = new DeptServiceImpl(sqlClient);
        UserService userService = new UserServiceImpl(roleService, sqlClient, redis);
        PermissionProvider hasPermissionLogic = new HasPermissionLogic("user:view", userService);
        webStarter.addOpenApiRoute("LoginByUsernameHandler", new LoginByUsernameHandler(redis, config(), userService, tokenProvider));
        webStarter.addOpenApiRoute("UserPermissionsHandler", new UserPermissionsHandler(userService));
        webStarter.addOpenApiRoute("UserRoutersHandler", new UserRoutersHandler(userService));
        webStarter.addOpenApiRoute("UserPageHandler", new UserPageHandler(hasPermissionLogic, userService));
        webStarter.addOpenApiRoute("DeptTreeHandler", new DeptTreeHandler(hasPermissionLogic, deptService));
        webStarter.addOpenApiRoute("test", new TestHandler(userService));
        webStarter.addRoute(HttpMethod.GET, "/health", ctx -> RoutingContextUtil.success(ctx, "ok"));
        webStarter.errorHandler(404, (ctx) -> RoutingContextUtil.error(ctx, "接口未找到"));
    }


}
