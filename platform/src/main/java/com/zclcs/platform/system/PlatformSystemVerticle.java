package com.zclcs.platform.system;

import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.GlobalHandler;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.redis.starter.rate.limit.impl.DefaultRateLimiterClient;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.starter.WebStarter;
import com.zclcs.platform.system.handler.*;
import com.zclcs.platform.system.security.DefaultStintLogic;
import com.zclcs.platform.system.security.HasPermissionLogic;
import com.zclcs.platform.system.service.DeptService;
import com.zclcs.platform.system.service.RoleService;
import com.zclcs.platform.system.service.UserService;
import com.zclcs.platform.system.service.impl.DeptServiceImpl;
import com.zclcs.platform.system.service.impl.RoleServiceImpl;
import com.zclcs.platform.system.service.impl.UserServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestPredicateException;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    private final RedisAPI redis;

    private final SqlClient sqlClient;

    private final JsonObject env;

    private final int httpPort;

    private final String httpHost;

    public PlatformSystemVerticle(JsonObject env, SqlClient sqlClient, RedisAPI redis, int httpPort, String httpHost) {
        this.env = env;
        this.sqlClient = sqlClient;
        this.redis = redis;
        this.httpPort = httpPort;
        this.httpHost = httpHost;
    }

    @Override
    public void start() {
        WebStarter webStarter = new WebStarter(vertx, env) {
            @Override
            public void initRoute(Router router) {
                PlatformSystemVerticle.this.initRoute(router);
            }
        };
        webStarter.setUpHttpServer(
                        httpPort,
                        httpHost,
                        ctx -> {
                            Throwable failure = ctx.failure();
                            int httpStatus = HttpStatus.HTTP_INTERNAL_ERROR;
                            String msg = "系统异常";
                            if (failure instanceof BadRequestException) {
                                httpStatus = HttpStatus.HTTP_BAD_REQUEST;
                                if (failure instanceof ParameterProcessorException e) {
                                    msg = e.getMessage();
                                } else if (failure instanceof BodyProcessorException e) {
                                    msg = e.getMessage();
                                } else if (failure instanceof RequestPredicateException e) {
                                    msg = e.getMessage();
                                }
                            } else {
                                log.error("全局异常捕捉 {}", failure.getMessage(), failure);
                            }
                            RoutingContextUtil.error(ctx, httpStatus, msg);
                        },
                        Map.of(
                                HttpStatus.HTTP_NOT_FOUND, (ctx) -> RoutingContextUtil.error(ctx, HttpStatus.HTTP_NOT_FOUND, "请求路径未找到"),
                                HttpStatus.HTTP_BAD_METHOD, (ctx) -> RoutingContextUtil.error(ctx, HttpStatus.HTTP_BAD_METHOD, "请求方法未找到"),
                                HttpStatus.HTTP_NOT_ACCEPTABLE, (ctx) -> RoutingContextUtil.error(ctx, HttpStatus.HTTP_NOT_ACCEPTABLE, "请求头错误"),
                                HttpStatus.HTTP_UNSUPPORTED_TYPE, (ctx) -> RoutingContextUtil.error(ctx, HttpStatus.HTTP_UNSUPPORTED_TYPE, "请求内容类型错误"),
                                HttpStatus.HTTP_BAD_REQUEST, (ctx) -> RoutingContextUtil.error(ctx, HttpStatus.HTTP_BAD_REQUEST, "请求体为空")
                        )
                )
                .onSuccess(server -> {
                    log.info("http server start at {}:{}", httpHost, httpPort);
                })
                .onFailure(e -> {
                    log.error("start http server error {}", e.getMessage(), e);
                    vertx.close();
                })
        ;
    }

    public void initRoute(Router router) {
        SchemaParser parser = SchemaParser.createDraft7SchemaParser(
                SchemaRouter.create(vertx, new SchemaRouterOptions())
        );
        TokenProvider tokenProvider = new RedisTokenLogic(redis);
        DefaultStintLogic stintProvider = new DefaultStintLogic(config(), redis, sqlClient);
        DefaultRateLimiterClient defaultRateLimiterClient = new DefaultRateLimiterClient(vertx, redis);
        RoleService roleService = new RoleServiceImpl(sqlClient);
        DeptService deptService = new DeptServiceImpl(sqlClient);
        UserService userService = new UserServiceImpl(roleService, sqlClient, redis);
        PermissionProvider hasPermissionLogic = new HasPermissionLogic("user:view", userService);
        router.route("/*").handler(new GlobalHandler(tokenProvider, defaultRateLimiterClient, stintProvider));
        VerifyCodeHandler verifyCodeHandler = new VerifyCodeHandler(redis, parser);
        router.get("/code").handler(verifyCodeHandler.validationHandler).handler(verifyCodeHandler);
        router.post("/login/token/byUsername").handler(new LoginByUsernameHandler(redis, config(), userService, tokenProvider));
        router.get("/user/permissions").handler(new UserPermissionsHandler(userService));
        router.get("/user/routers").handler(new UserRoutersHandler(userService));
        router.get("/user").handler(new UserPageHandler(hasPermissionLogic, userService));
        router.get("/dept/tree").handler(new DeptTreeHandler(hasPermissionLogic, deptService));
        router.get("/test").handler(new TestHandler(userService));
        router.get("/health").handler(ctx -> RoutingContextUtil.success(ctx, "ok"));
    }


}
