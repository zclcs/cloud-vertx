package com.zclcs.platform.system;

import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.GlobalHandler;
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
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.json.schema.draft7.dsl.Keywords;
import io.vertx.json.schema.draft7.dsl.Schemas;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.vertx.ext.web.validation.builder.Parameters.param;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    private WebStarter webStarter;

    private final TokenProvider tokenProvider;

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
        this.tokenProvider = new RedisTokenLogic(redis);
    }

    @Override
    public void start() {
        webStarter = new WebStarter(vertx, env) {
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
                            String msg = "系统异常";
                            if (failure instanceof BadRequestException) {
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
                            RoutingContextUtil.error(ctx, msg);
                        },
                        Map.of(
                                404, (ctx) -> RoutingContextUtil.error(ctx, "请求路径未找到"),
                                405, (ctx) -> RoutingContextUtil.error(ctx, "请求方法未找到"),
                                406, (ctx) -> RoutingContextUtil.error(ctx, "请求头错误"),
                                415, (ctx) -> RoutingContextUtil.error(ctx, "请求内容类型错误"),
                                400, (ctx) -> RoutingContextUtil.error(ctx, "请求体为空")
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
        DefaultStintLogic stintProvider = new DefaultStintLogic(config(), redis, sqlClient);
        DefaultRateLimiterClient defaultRateLimiterClient = new DefaultRateLimiterClient(vertx, redis);
        RoleService roleService = new RoleServiceImpl(sqlClient);
        DeptService deptService = new DeptServiceImpl(sqlClient);
        UserService userService = new UserServiceImpl(roleService, sqlClient, redis);
        PermissionProvider hasPermissionLogic = new HasPermissionLogic("user:view", userService);
        router.route("/*").handler(new GlobalHandler(tokenProvider, defaultRateLimiterClient, stintProvider));
        SchemaParser parser = SchemaParser.createDraft7SchemaParser(
                SchemaRouter.create(vertx, new SchemaRouterOptions())
        );
        router.get("/code").handler(ValidationHandlerBuilder
                .create(parser)
                .queryParameter(param("randomStr", Schemas.stringSchema().with(Keywords.maxLength(20))))
                .build()).handler(new VerifyCodeHandler(redis));
        router.post("/login/token/byUsername").handler(new LoginByUsernameHandler(redis, config(), userService, tokenProvider));
        router.get("/user/permissions").handler(new UserPermissionsHandler(userService));
        router.get("/user/routers").handler(new UserRoutersHandler(userService));
        router.get("/user").handler(new UserPageHandler(hasPermissionLogic, userService));
        router.get("/dept/tree").handler(new DeptTreeHandler(hasPermissionLogic, deptService));
        router.get("/test").handler(new TestHandler(userService));
        router.get("/health").handler(ctx -> RoutingContextUtil.success(ctx, "ok"));
    }


}
