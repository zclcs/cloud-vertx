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

import java.util.List;

/**
 * @author zclcs
 */
public class AddUserBatchHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    public AddUserBatchHandler(PermissionProvider permissionProvider, UserService userService) {
        super(permissionProvider);
        this.userService = userService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        RequestBody body = ctx.body();
        String string = body.asString();
        List<UserAo> userAos = JsonUtil.readList(string, UserAo.class);
        RoutingContextUtil.success(ctx, "success");
    }


}
