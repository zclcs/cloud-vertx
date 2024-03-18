package com.zclcs.platform.system;

import com.zclcs.cloud.lib.security.logic.RedisTokenLogic;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.GlobalHandler;
import com.zclcs.common.core.constant.HttpStatus;
import com.zclcs.common.redis.starter.rate.limit.impl.DefaultRateLimiterClient;
import com.zclcs.common.security.provider.PermissionProvider;
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
        SchemaParser parser = SchemaParser.createDraft7SchemaParser(
                SchemaRouter.create(vertx, new SchemaRouterOptions())
        );
        TokenProvider tokenProvider = new RedisTokenLogic(redis);
        DefaultStintLogic stintProvider = new DefaultStintLogic(config(), redis, pool);
        DefaultRateLimiterClient defaultRateLimiterClient = new DefaultRateLimiterClient(vertx, redis);
        DictItemService dictItemService = new DictItemServiceImpl(pool, redis);
        RoleService roleService = new RoleServiceImpl(pool);
        DeptService deptService = new DeptServiceImpl(pool);
        UserRoleService userRoleService = new UserRoleServiceImpl(pool);
        UserDataPermissionService userDataPermissionService = new UserDataPermissionServiceImpl(pool);
        UserService userService = new UserServiceImpl(roleService, userRoleService, userDataPermissionService, dictItemService, pool, redis);
        PermissionProvider hasPermissionLogic = new HasPermissionLogic(userService, "user:view");
        VerifyCodeHandler verifyCodeHandler = new VerifyCodeHandler(redis, parser);

        router.route("/*").handler(new GlobalHandler(tokenProvider, defaultRateLimiterClient, stintProvider));

        router.get("/system/code").handler(verifyCodeHandler.validationHandler).handler(verifyCodeHandler);

        router.post("/system/login/token/byUsername").handler(new LoginByUsernameHandler(redis, config(), userService, tokenProvider));

        router.get("/system/user/permissions").handler(new UserPermissionsHandler(userService));
        router.get("/system/user/routers").handler(new UserRoutersHandler(userService));
        router.get("/system/user").handler(new UserPageHandler(hasPermissionLogic, userService));
        router.get("/system/user/list").handler(new UserListHandler(hasPermissionLogic, userService));
        router.get("/system/user/one").handler(new UserOneHandler(hasPermissionLogic, userService));
        router.post("/system/user").handler(new AddUserHandler(new HasPermissionLogic(userService, "user:add"), userService));
        router.put("/system/user").handler(new UpdateUserHandler(new HasPermissionLogic(userService, "user:update"), userService));
        router.delete("/system/user/:userIds").handler(new DeleteUserHandler(new HasPermissionLogic(userService, "user:delete"), userService));
        router.get("/system/user/checkUsername").handler(new CheckUsernameHandler(new HasAnyPermissionLogic(userService, "user:add", "user:update"), userService));
        router.get("/system/user/checkUserMobile").handler(new CheckUserMobileHandler(new HasAnyPermissionLogic(userService, "user:add", "user:update"), userService));
        router.post("/system/user/batch").handler(new AddUserBatchHandler(new HasPermissionLogic(userService, "user:add:batch"), userService));

        router.get("/system/role/options").handler(new RoleOptionsHandler(new HasAnyPermissionLogic(userService, "user:view", "role:view"), roleService));

        router.get("/system/dept/tree").handler(new DeptTreeHandler(hasPermissionLogic, deptService));
        router.get("/system/dept/options").handler(new DeptOptionsHandler(new HasAnyPermissionLogic(userService, "user:view", "dept:view"), deptService));

        router.get("/system/dict/dictQuery").handler(new DictQueryHandler(dictItemService));
        router.get("/system/dict/dictTextQuery").handler(new DictQueryHandler(dictItemService));

        router.get("/system/test").handler(new TestHandler(userService));
        router.get("/system/health").handler(ctx -> RoutingContextUtil.success(ctx, "ok"));
    }


}
