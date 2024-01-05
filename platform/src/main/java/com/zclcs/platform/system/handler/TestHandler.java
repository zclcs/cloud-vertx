package com.zclcs.platform.system.handler;

import com.zclcs.cloud.lib.web.utils.WebUtil;
import com.zclcs.common.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;

/**
 * @author zclcs
 */
public class TestHandler implements Handler<RoutingContext> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private final UserService userService;

    public TestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String username = ctx.request().getParam("username");
        userService.getUser(username).onComplete(user -> {
            if (user != null) {
                RoutingContextUtil.success(ctx, WebUtil.data(user));
            } else {
                RoutingContextUtil.error(ctx, "账号密码错误");
            }
        }, e -> {
            RoutingContextUtil.error(ctx, "系统异常");
        });
    }

}
