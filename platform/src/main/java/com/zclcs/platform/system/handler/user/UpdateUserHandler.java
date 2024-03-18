package com.zclcs.platform.system.handler.user;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.dao.ao.UserAo;
import com.zclcs.platform.system.service.UserService;
import io.vertx.ext.web.RequestBody;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class UpdateUserHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    public UpdateUserHandler(PermissionProvider permissionProvider, UserService userService) {
        super(permissionProvider);
        this.userService = userService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        RequestBody body = ctx.body();
        String bodyString = body.asString();
        UserAo userAo = JsonUtil.readValue(bodyString, UserAo.class);
        userService.updateUser(userAo).onComplete(user -> {
            RoutingContextUtil.success(ctx, user);
        }, error -> {
            log.error("修改用户失败", error);
            RoutingContextUtil.error(ctx, "修改用户失败");
        });
    }


}
