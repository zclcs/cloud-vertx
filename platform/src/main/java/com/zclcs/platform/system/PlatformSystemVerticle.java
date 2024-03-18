package com.zclcs.platform.system;

import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.GlobalHandler;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.redis.starter.rate.limit.impl.DefaultRateLimiterClient;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.common.web.starter.WebStarter;
import com.zclcs.platform.system.handler.common.TestHandler;
import com.zclcs.platform.system.handler.common.VerifyCodeHandler;
import com.zclcs.platform.system.handler.dept.DeptOptionsHandler;
import com.zclcs.platform.system.handler.dept.DeptTreeHandler;
import com.zclcs.platform.system.handler.dict.DictQueryHandler;
import com.zclcs.platform.system.handler.login.LoginByUsernameHandler;
import com.zclcs.platform.system.handler.role.RoleOptionsHandler;
import com.zclcs.platform.system.handler.user.*;
import com.zclcs.platform.system.security.DefaultStintLogic;
import com.zclcs.platform.system.security.HasAnyPermissionLogic;
import com.zclcs.platform.system.security.HasPermissionLogic;
import com.zclcs.platform.system.service.*;
import com.zclcs.platform.system.service.impl.*;
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
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zclcs
 */
public class PlatformSystemVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(PlatformSystemVerticle.class);

    private final RedisAPI redis;

    private final Pool pool;

    private final JsonObject env;

    private final int httpPort;

    private final String httpHost;

    public PlatformSystemVerticle(JsonObject env, Pool pool, RedisAPI redis, int httpPort, String httpHost) {
        this.env = env;
        this.pool = pool;
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
        TokenProvider tokenProvider = new RedisTokenLogic(redis);
        DefaultStintLogic stintProvider = new DefaultStintLogic(config(), redis, pool);
        DefaultRateLimiterClient defaultRateLimiterClient = new DefaultRateLimiterClient(vertx, redis);

        router.route("/*").handler(new GlobalHandler(tokenProvider, defaultRateLimiterClient, stintProvider));

        router.route("/system/*")
                .subRouter(initSubRouter(tokenProvider));
    }

    private Router initSubRouter(TokenProvider tokenProvider) {
        Router router = Router.router(vertx);

        SchemaParser parser = SchemaParser.createDraft7SchemaParser(
                SchemaRouter.create(vertx, new SchemaRouterOptions())
        );
        DictItemService dictItemService = new DictItemServiceImpl(pool, redis);
        RoleService roleService = new RoleServiceImpl(pool);
        DeptService deptService = new DeptServiceImpl(pool);
        UserRoleService userRoleService = new UserRoleServiceImpl(pool);
        UserDataPermissionService userDataPermissionService = new UserDataPermissionServiceImpl(pool);
        UserService userService = new UserServiceImpl(roleService, userRoleService, userDataPermissionService, dictItemService, pool, redis);
        VerifyCodeHandler verifyCodeHandler = new VerifyCodeHandler(redis, parser);

        router.get("/code").handler(verifyCodeHandler.validationHandler).handler(verifyCodeHandler);

        router.post("/login/token/byUsername").handler(new LoginByUsernameHandler(redis, config(), userService, tokenProvider));

        router.get("/user/permissions").handler(new UserPermissionsHandler(userService));
        router.get("/user/routers").handler(new UserRoutersHandler(userService));
        router.get("/user").handler(new UserPageHandler(new HasPermissionLogic(userService, "user:view"), userService));
        router.get("/user/list").handler(new UserListHandler(new HasPermissionLogic(userService, "user:view"), userService));
        router.get("/user/one").handler(new UserOneHandler(new HasPermissionLogic(userService, "user:view"), userService));
        router.post("/user").handler(new AddUserHandler(new HasPermissionLogic(userService, "user:add"), userService));
        router.put("/user").handler(new UpdateUserHandler(new HasPermissionLogic(userService, "user:update"), userService));
        router.delete("/user/:userIds").handler(new DeleteUserHandler(new HasPermissionLogic(userService, "user:delete"), userService));
        router.get("/user/checkUsername").handler(new CheckUsernameHandler(new HasAnyPermissionLogic(userService, "user:add", "user:update"), userService));
        router.get("/user/checkUserMobile").handler(new CheckUserMobileHandler(new HasAnyPermissionLogic(userService, "user:add", "user:update"), userService));
        router.post("/user/batch").handler(new AddUserBatchHandler(new HasPermissionLogic(userService, "user:add:batch"), userService));

        router.get("/role/options").handler(new RoleOptionsHandler(new HasAnyPermissionLogic(userService, "user:view", "role:view"), roleService));

        router.get("/dept/tree").handler(new DeptTreeHandler(new HasAnyPermissionLogic(userService, "user:view", "dept:view"), deptService));
        router.get("/dept/options").handler(new DeptOptionsHandler(new HasAnyPermissionLogic(userService, "user:view", "dept:view"), deptService));

        router.get("/dict/dictQuery").handler(new DictQueryHandler(dictItemService));
        router.get("/dict/dictTextQuery").handler(new DictQueryHandler(dictItemService));

        router.get("/test").handler(new TestHandler(userService));
        router.get("/health").handler(ctx -> RoutingContextUtil.success(ctx, "ok"));

        return router;
    }

}
