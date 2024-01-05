package com.zclcs.platform.system.handler;

import com.zclcs.common.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.sqlclient.Pool;
import org.slf4j.Logger;

/**
 * @author zclcs
 */
public class TestHandler implements Handler<RoutingContext> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private final Pool dbClient;

    private final ServiceDiscovery serviceDiscovery;

    public TestHandler(Pool mysqlClient, ServiceDiscovery serviceDiscovery) {
        this.dbClient = mysqlClient;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String username = ctx.request().getParam("username");
        EventBusService.getProxy(serviceDiscovery, UserService.class, ar -> {
            UserService userService = ar.result();
            if (userService != null) {
                userService.getUser(username, handler -> {
                    if (handler.succeeded()) {
                        RoutingContextUtil.success(ctx, Json.encode(handler.result()));
                    } else {
                        RoutingContextUtil.success(ctx, "用户名或密码错误");
                    }
                });
            } else {
                RoutingContextUtil.success(ctx, "用户名或密码错误");
            }
        });
    }

}
