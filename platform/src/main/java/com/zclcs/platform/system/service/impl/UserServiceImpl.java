package com.zclcs.platform.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zclcs.cloud.core.base.Page;
import com.zclcs.cloud.core.base.PageAo;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.core.constant.YesOrNo;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.cache.MenuCacheVo;
import com.zclcs.platform.system.dao.cache.UserCacheVo;
import com.zclcs.platform.system.dao.entity.MenuRowMapper;
import com.zclcs.platform.system.dao.entity.User;
import com.zclcs.platform.system.dao.entity.UserRowMapper;
import com.zclcs.platform.system.dao.router.RouterMeta;
import com.zclcs.platform.system.dao.router.VueRouter;
import com.zclcs.platform.system.dao.vo.UserVo;
import com.zclcs.platform.system.service.UserService;
import com.zclcs.platform.system.utils.RouterUtil;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

/**
 * @author zclcs
 */
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final SqlClient sqlClient;
    private final RedisAPI redis;

    private final Duration redisExpire = Duration.ofDays(1);

    public UserServiceImpl(SqlClient sqlClient, RedisAPI redis) {
        this.sqlClient = sqlClient;
        this.redis = redis;
    }

    private final LocalCache<String, UserCacheVo> userCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    private final LocalCache<String, List<String>> permissionCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    private final LocalCache<String, List<VueRouter<MenuCacheVo>>> routerCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    @Override
    public Future<User> getUser(String username) {
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT * FROM system_user WHERE username = #{loginId}
                        """)
                .mapTo(UserRowMapper.INSTANCE)
                .execute(Collections.singletonMap("loginId", username))
                .map(rs -> {
                    if (rs.iterator().hasNext()) {
                        return rs.iterator().next();
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public Future<UserCacheVo> getUserCache(String username) {
        String k = String.format(RedisPrefix.USER_PREFIX, username);
        return userCache.getIfPresent(k).compose(v -> {
            if (v != null) {
                return Future.succeededFuture(v);
            } else {
                return redis.get(k).compose(rv -> {
                    if (rv != null) {
                        UserCacheVo user = Json.decodeValue(rv.toString(), UserCacheVo.class);
                        userCache.put(k, user);
                        return Future.succeededFuture(user);
                    } else {
                        return getUser(username).map(dv -> {
                            if (dv != null) {
                                return new UserCacheVo(dv);
                            } else {
                                return null;
                            }
                        }).compose(dv -> {
                            if (dv != null) {
                                String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                                redis.set(List.of(k, JsonUtil.toJson(dv), "EX", expireTime));
                                return Future.succeededFuture(dv);
                            } else {
                                return Future.succeededFuture(null);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public Future<List<String>> getUserPermissionCache(String username) {
        String k = String.format(RedisPrefix.USER_PERMISSION_PREFIX, username);
        return permissionCache.getIfPresent(k).compose(v -> {
            if (v != null) {
                return Future.succeededFuture(v);
            } else {
                return redis.get(k).compose(rv -> {
                    if (rv != null) {
                        List<String> permissions = JsonUtil.readList(rv.toString(), String.class);
                        permissionCache.put(k, permissions);
                        return Future.succeededFuture(permissions);
                    } else {
                        return SqlTemplate.forQuery(sqlClient, """
                                        SELECT distinct system_menu.perms FROM system_menu 
                                            INNER JOIN system_role_menu ON system_menu.menu_id = system_role_menu.menu_id 
                                            INNER JOIN system_role ON system_role_menu.role_id = system_role.role_id 
                                            INNER JOIN system_user_role ON system_role.role_id = system_user_role.role_id 
                                            INNER JOIN system_user ON system_user_role.user_id = system_user.user_id
                                        WHERE system_user.username = #{loginId} and system_menu.perms != ''
                                        """)
                                .execute(Collections.singletonMap("loginId", username))
                                .map(rs -> {
                                    List<String> permissions = new ArrayList<>();
                                    rs.forEach(row -> {
                                        permissions.add(row.getString(0));
                                    });
                                    return permissions;
                                })
                                .compose(dv -> {
                                    String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                                    redis.set(List.of(k, JsonUtil.toJson(dv), "EX", expireTime));
                                    return Future.succeededFuture(dv);
                                });
                    }
                });
            }
        });
    }

    @Override
    public Future<List<VueRouter<MenuCacheVo>>> getUserRouterCache(String username) {
        String userRouterPrefix = String.format(RedisPrefix.USER_ROUTER_PREFIX, username);
        return routerCache.getIfPresent(userRouterPrefix)
                .compose(v -> {
                    if (v != null) {
                        return Future.succeededFuture(v);
                    } else {
                        return redis.get(userRouterPrefix).compose(rv -> {
                            if (rv != null) {
                                TypeReference<List<VueRouter<MenuCacheVo>>> typeReference = new TypeReference<>() {
                                };
                                List<VueRouter<MenuCacheVo>> vueRouters = JsonUtil.readValue(rv.toString(), typeReference);
                                routerCache.put(userRouterPrefix, vueRouters);
                                return Future.succeededFuture(vueRouters);
                            } else {
                                return SqlTemplate.forQuery(sqlClient, """
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
                                                        WHERE system_user.username = #{loginId} and system_menu.type != '1' order by system_menu.order_num 
                                                """)
                                        .mapTo(MenuRowMapper.INSTANCE)
                                        .execute(Collections.singletonMap("loginId", username))
                                        .map(rows -> {
                                            List<VueRouter<MenuCacheVo>> vueRouters = new ArrayList<>();
                                            rows.forEach(menu -> {
                                                VueRouter<MenuCacheVo> route = new VueRouter<>();
                                                route.setId(menu.getMenuId());
                                                route.setCode(menu.getMenuCode());
                                                route.setParentCode(menu.getParentCode());
                                                route.setPath(menu.getPath());
                                                route.setName(StringsUtil.isNotBlank(menu.getKeepAliveName()) ? menu.getKeepAliveName() : menu.getMenuName());
                                                route.setComponent(menu.getComponent());
                                                route.setRedirect(menu.getRedirect());
                                                route.setMeta(new RouterMeta(
                                                        menu.getMenuName(),
                                                        menu.getIcon(),
                                                        YesOrNo.YES.equals(menu.getHideMenu()),
                                                        YesOrNo.YES.equals(menu.getIgnoreKeepAlive()),
                                                        YesOrNo.YES.equals(menu.getHideBreadcrumb()),
                                                        YesOrNo.YES.equals(menu.getHideChildrenInMenu()),
                                                        menu.getCurrentActiveMenu()));
                                                vueRouters.add(route);
                                            });
                                            return RouterUtil.buildVueRouter(vueRouters);
                                        })
                                        .compose(dv -> {
                                            String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                                            redis.set(List.of(userRouterPrefix, JsonUtil.toJson(dv), "EX", expireTime));
                                            return Future.succeededFuture(dv);
                                        })
                                        ;
                            }
                        });
                    }
                })
                ;
    }

    @Override
    public Future<Boolean> deleteUserRelatedCache(String username) {
        return redis.del(List.of(
                        String.format(RedisPrefix.USER_PREFIX, username),
                        String.format(RedisPrefix.USER_PERMISSION_PREFIX, username),
                        String.format(RedisPrefix.USER_ROUTER_PREFIX, username)
                ))
                .map(v -> true);
    }

    @Override
    public Future<Page<UserVo>> getUserPage(UserVo userVo, PageAo pageAo) {
        Long pageNum = pageAo.getPageNum();
        Long pageSize = pageAo.getPageSize();
        String sql = """
                SELECT user_id, 
                            username, 
                            real_name, 
                            password, 
                            dept_id, 
                            email, 
                            mobile, 
                            status, 
                            last_login_time, 
                            gender, is_tab, 
                            theme, 
                            avatar, 
                            description, 
                            create_at
                            FROM system_user 
                            where 1=1
                """;
        if (StringsUtil.isNotBlank(userVo.getUsername())) {
            sql += " username = #{username} ";
        }
        sql += " limit #{pageNum}, #{pageSize}";
        Map<String, Object> params = new HashMap<>();
        if (StringsUtil.isNotBlank(userVo.getUsername())) {
            params.put("username", userVo.getUsername());
        }
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        return SqlTemplate.forQuery(sqlClient, sql)
                .mapTo(UserRowMapper.INSTANCE)
                .execute(params)
                .map(rows -> {
                    if (rows != null && rows.size() > 0) {
                        List<UserVo> list = new ArrayList<>();
                        rows.forEach(user -> {
                            UserVo vo = new UserVo(user);
                            list.add(vo);
                        });
                        return new Page<>(pageNum, pageSize, (long) rows.size(), list);
                    } else {
                        return new Page<>(pageNum, pageSize);
                    }
                })
                ;
    }


}
