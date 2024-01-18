package com.zclcs.platform.system.handler;

import com.zclcs.cloud.core.base.PageAo;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.dao.vo.UserVo;
import com.zclcs.platform.system.service.UserService;
import io.vertx.ext.web.RoutingContext;
import io.vertx.openapi.validation.ValidatedRequest;

import static io.vertx.ext.web.openapi.router.RouterBuilder.KEY_META_DATA_VALIDATED_REQUEST;

/**
 * @author zclcs
 */
public class UserPageHandler extends BasePermissionHandler {

    private final UserService userService;

    public UserPageHandler(PermissionProvider permissionProvider, UserService userService) {
        super(permissionProvider);
        this.userService = userService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        ValidatedRequest validatedRequest = ctx.get(KEY_META_DATA_VALIDATED_REQUEST);
        Long pageNum = validatedRequest.getQuery().get("pageNum").getLong();
        Long pageSize = validatedRequest.getQuery().get("pageSize").getLong();
        String username = validatedRequest.getQuery().get("username").getString();
        UserVo userVo = new UserVo();
        userVo.setUsername(username);
        userService.getUserPage(userVo, new PageAo(pageNum, pageSize))
                .onComplete(res -> {
                    RoutingContextUtil.success(ctx, res);
                })
        ;

    }
}
