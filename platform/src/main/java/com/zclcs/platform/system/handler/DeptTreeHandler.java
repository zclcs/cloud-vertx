package com.zclcs.platform.system.handler;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.dao.vo.DeptVo;
import com.zclcs.platform.system.service.DeptService;
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
public class DeptTreeHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DeptService deptService;

    public DeptTreeHandler(PermissionProvider permissionProvider, DeptService deptService) {
        super(permissionProvider);
        this.deptService = deptService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        ValidatedRequest validatedRequest = ctx.get(KEY_META_DATA_VALIDATED_REQUEST);
        Map<String, RequestParameter> query = validatedRequest.getQuery();
        String deptName = query.get("deptName").getString();
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
