package com.zclcs.platform.system.utils;

import com.zclcs.cloud.core.constant.CommonCore;
import com.zclcs.platform.system.dao.router.VueRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zclcs
 */
public class VueRouterUtil {


    /**
     * 构造前端路由
     *
     * @param routes routes
     * @param <T>    T
     * @return ArrayList<VueRouter < T>>
     */
    public static <T> List<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null || routes.isEmpty()) {
            return null;
        }
        List<VueRouter<T>> topRoutes = new ArrayList<>();
        routes.forEach(route -> {
            String parentCode = route.getParentCode();
            if (parentCode == null || CommonCore.TOP_PARENT_CODE.equals(parentCode)) {
                topRoutes.add(route);
                return;
            }
            for (VueRouter<T> parent : routes) {
                String code = parent.getCode();
                if (code != null && code.equals(parentCode)) {
                    if (parent.getChildren() == null) {
                        parent.initChildren();
                    }
                    parent.getChildren().add(route);
                    parent.setHasChildren(true);
                    route.setHasParent(true);
                    parent.setHasParent(true);
                    return;
                }
            }
        });
        return topRoutes;
    }
}