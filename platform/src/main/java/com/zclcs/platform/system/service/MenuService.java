package com.zclcs.platform.system.service;

import com.zclcs.platform.system.dao.cache.MenuCacheVo;
import com.zclcs.platform.system.dao.entity.Menu;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author zhouc
 */
public interface MenuService {

    /**
     * 获取用户菜单
     *
     * @param username 用户名
     * @return 菜单
     */
    Future<List<Menu>> getUserMenu(String username);

    /**
     * 获取用户菜单缓存
     *
     * @param username 用户名
     * @return 菜单缓存
     */
    Future<List<MenuCacheVo>> getUserMenuCache(String username);

}
