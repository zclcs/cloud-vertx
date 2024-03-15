package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.cache.MenuCacheVo;
import com.zclcs.platform.system.dao.entity.Menu;
import com.zclcs.platform.system.dao.entity.MenuRowMapper;
import com.zclcs.platform.system.service.MenuService;
import com.zclcs.sql.helper.service.impl.BaseSqlService;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Future;

public class MenuServiceImpl extends BaseSqlService<Menu> implements MenuService {

    private final Pool pool;
    private final RedisAPI redis;

    private final Duration redisMenuExpire = Duration.ofDays(1);

    public MenuServiceImpl(Pool pool, RedisAPI redis) {
        super(pool, MenuRowMapper.INSTANCE, Menu.class);
        this.pool = pool;
        this.redis = redis;
    }

    @Override
    public Future<List<Menu>> getUserMenu(String username) {
        SqlTemplate.forQuery(pool, """
                SELECT  system_menu.menu_id, 
                        system_menu.parent_code, 
                        system_menu.menu_name, 
                        system_menu.menu_code, 
                        system_menu.keep_alive_name, 
                        system_menu.path, 
                        system_menu.icon, 
                        system_menu.component, 
                        system_menu.redirect, 
                        system_menu.perms, 
                        system_menu.type, 
                        system_menu.hide_menu, 
                        system_menu.ignore_keep_alive, 
                        system_menu.hide_breadcrumb, 
                        system_menu.hide_children_in_menu, 
                        system_menu.current_active_menu, 
                        system_menu.order_num 
                        FROM system_menu 
                             INNER JOIN system_role_menu ON system_menu.menu_id = system_role_menu.menu_id 
                             INNER JOIN system_role ON system_role_menu.role_id = system_role.role_id 
                             INNER JOIN system_user_role ON system_role.role_id = system_user_role.role_id 
                             INNER JOIN system_user ON system_user_role.user_id = system_user.user_id
                        WHERE system_user.username = #{loginId}
                """);
        return null;
    }

    @Override
    public Future<List<MenuCacheVo>> getUserMenuCache(String username) {
        return null;
    }
}
