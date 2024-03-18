package com.zclcs.platform.system.handler.dept;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.cloud.security.BasePermissionHandler;
import com.zclcs.common.security.provider.PermissionProvider;
import com.zclcs.platform.system.dao.vo.DeptVo;
import com.zclcs.platform.system.service.DeptService;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zclcs
 */
public class DeptOptionsHandler extends BasePermissionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DeptService deptService;

    public DeptOptionsHandler(PermissionProvider permissionProvider, DeptService deptService) {
        super(permissionProvider);
        this.deptService = deptService;
    }

    @Override
    public void doNext(RoutingContext ctx) {
        String deptName = ctx.request().getParam("deptName");
        DeptVo deptVo = new DeptVo();
        deptVo.setDeptName(deptName);
        deptService.getDeptVoList(deptVo)
                .onComplete(deptTree -> {
                    RoutingContextUtil.success(ctx, deptTree);
                }, e -> {
                    log.error("查询失败", e);
                    RoutingContextUtil.error(ctx, "查询失败");
                })
        ;
    }
}
