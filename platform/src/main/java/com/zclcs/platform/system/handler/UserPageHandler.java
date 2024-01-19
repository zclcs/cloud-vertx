package com.zclcs.platform.system.handler;

import com.zclcs.cloud.core.base.PageAo;
import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.dao.vo.UserVo;
import com.zclcs.platform.system.service.UserService;
import io.vertx.ext.web.RoutingContext;
import io.vertx.openapi.validation.RequestParameter;
import io.vertx.openapi.validation.ValidatedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.vertx.ext.web.openapi.router.RouterBuilder.KEY_META_DATA_VALIDATED_REQUEST;

/**
 * @author zclcs
 */
public class UserPageHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    public UserPageHandler(PermissionProvider permissionProvider, UserService userService) {
        super(permissionProvider);
        this.userService = userService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        ValidatedRequest validatedRequest = ctx.get(KEY_META_DATA_VALIDATED_REQUEST);
        Map<String, RequestParameter> query = validatedRequest.getQuery();
        Long pageNum = query.get("pageNum").getLong();
        Long pageSize = query.get("pageSize").getLong();
        String username = query.get("username").getString();
        UserVo userVo = new UserVo();
        userVo.setUsername(username);
        userService.getUserPage(userVo, new PageAo(pageNum, pageSize))
                .onComplete(userVoPage -> {
                    RoutingContextUtil.success(ctx, userVoPage);
                }, e -> {
                    log.error("查询失败", e);
                    RoutingContextUtil.error(ctx, "查询失败");
                })
        ;

    }
}
