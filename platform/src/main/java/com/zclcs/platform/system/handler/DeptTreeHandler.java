package com.zclcs.platform.system.handler;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.cloud.security.StintProvider;
import com.zclcs.common.redis.starter.rate.limit.RateLimiterClient;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.common.security.provider.TokenProvider;
import com.zclcs.platform.system.dao.vo.DeptVo;
import com.zclcs.platform.system.service.DeptService;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class DeptTreeHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DeptService deptService;

    public DeptTreeHandler(TokenProvider tokenProvider, RateLimiterClient rateLimiterClient, StintProvider stintProvider, PermissionProvider permissionProvider, DeptService deptService) {
        super(tokenProvider, rateLimiterClient, stintProvider, permissionProvider);
        this.deptService = deptService;
    }

    @Override
    public void completePermission(RoutingContext ctx) {
        String deptName = ctx.request().getParam("deptName");
        DeptVo deptVo = new DeptVo();
        deptVo.setDeptName(deptName);
        deptService.getDeptTree(deptVo)
                .onComplete(deptTree -> {
                    RoutingContextUtil.success(ctx, deptTree);
                }, e -> {
                    log.error("查询失败", e);
                    RoutingContextUtil.error(ctx, "查询失败");
                })
        ;
    }
}
