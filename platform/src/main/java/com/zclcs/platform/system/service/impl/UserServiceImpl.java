package com.zclcs.platform.system.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zclcs.cloud.core.constant.CommonCore;
import com.zclcs.cloud.core.constant.DictName;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.core.constant.YesOrNo;
import com.zclcs.cloud.security.SecurityContext;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.security.BCrypt;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.common.security.constant.LoginType;
import com.zclcs.platform.system.dao.ao.UserAo;
import com.zclcs.platform.system.dao.cache.MenuCacheVo;
import com.zclcs.platform.system.dao.cache.UserCacheVo;
import com.zclcs.platform.system.dao.entity.*;
import com.zclcs.platform.system.dao.router.RouterMeta;
import com.zclcs.platform.system.dao.router.VueRouter;
import com.zclcs.platform.system.dao.vo.UserVo;
import com.zclcs.platform.system.dao.vo.UserVoRowMapper;
import com.zclcs.platform.system.service.*;
import com.zclcs.platform.system.utils.VueRouterUtil;
import com.zclcs.sql.helper.service.impl.BaseSqlService;
import com.zclcs.sql.helper.statement.bean.Page;
import com.zclcs.sql.helper.statement.bean.PageAo;
import com.zclcs.sql.helper.statement.bean.SqlAndParams;
import com.zclcs.sql.helper.statement.bean.SqlAssist;
import com.zclcs.sql.helper.util.If;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

/**
 * @author zclcs
 */
public class UserServiceImpl extends BaseSqlService<User> implements UserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final UserDataPermissionService userDataPermissionService;
    private final Pool pool;
    private final RedisAPI redis;
    private final DictItemService dictItemService;
    private final Duration redisExpire = Duration.ofDays(1);

    public UserServiceImpl(RoleService roleService, UserRoleService userRoleService, UserDataPermissionService userDataPermissionService, DictItemService dictItemService, Pool pool, RedisAPI redis) {
        super(pool, UserRowMapper.INSTANCE, User.class);
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.userDataPermissionService = userDataPermissionService;
        this.dictItemService = dictItemService;
        this.pool = pool;
        this.redis = redis;
    }

    private final LocalCache<String, String> userMobileCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));
    private final LocalCache<String, UserCacheVo> userCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));
    private final LocalCache<String, List<String>> permissionCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));
    private final LocalCache<String, List<VueRouter<MenuCacheVo>>> routerCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    @Override
    public Future<UserCacheVo> getUserInfo(RoutingContext ctx) {
        String loginId = ctx.get(SecurityContext.LOGIN_ID);
        LoginType loginType = ctx.get(SecurityContext.LOGIN_TYPE, LoginType.username);
        return switch (loginType) {
            case mobile -> getUserCacheByMobile(loginId);
            default -> getUserCacheByName(loginId);
        };
    }

    @Override
    public Future<User> findById(Long id) {
        return getById(id);
    }

    @Override
    public Future<User> findByName(String username) {
        return getOne(new SqlAssist().andEq("username", username));
    }

    @Override
    public Future<User> findByMobile(String mobile) {
        return getOne(new SqlAssist().andEq("mobile", mobile));
    }

    @Override
    public Future<UserCacheVo> getUserCacheByName(String username) {
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
                        return findByName(username).map(dv -> {
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
    public Future<UserCacheVo> getUserCacheByMobile(String mobile) {
        return getUsernameByMobile(mobile).compose(username -> {
            if (StringsUtil.isBlank(username)) {
                return Future.succeededFuture(null);
            } else {
                return getUserCacheByName(username);
            }
        });
    }

    public Future<String> getUsernameByMobile(String mobile) {
        String k = String.format(RedisPrefix.USER_MOBILE_PREFIX, mobile);
        return userMobileCache.getIfPresent(k).compose(v -> {
            if (v != null) {
                return Future.succeededFuture(v);
            } else {
                return redis.get(k).compose(rv -> {
                    if (rv != null) {
                        String username = rv.toString();
                        userMobileCache.put(mobile, username);
                        return Future.succeededFuture(username);
                    } else {
                        return findByMobile(mobile).compose(dv -> {
                            if (dv != null) {
                                String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                                redis.set(List.of(k, dv.getUsername(), "EX", expireTime));
                                return Future.succeededFuture(dv.getUsername());
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
                        return SqlTemplate.forQuery(pool, """
                                        SELECT distinct system_menu.perms FROM system_menu 
                                            INNER JOIN system_role_menu ON system_menu.menu_id = system_role_menu.menu_id 
                                            INNER JOIN system_role ON system_role_menu.role_id = system_role.role_id 
                                            INNER JOIN system_user_role ON system_role.role_id = system_user_role.role_id 
                                            INNER JOIN system_user ON system_user_role.user_id = system_user.user_id
                                        WHERE system_user.username = #{loginId} and system_menu.perms != ''
                                        """)
                                .execute(Collections.singletonMap("loginId", username))
                                .flatMap(rs -> {
                                    List<String> permissions = new ArrayList<>();
                                    rs.forEach(row -> {
                                        permissions.add(row.getString(0));
                                    });
                                    return Future.succeededFuture(permissions);
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
                                return SqlTemplate.forQuery(pool, """
                                                SELECT system_menu.menu_id,
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
                                                  and system_menu.type != '1'
                                                group by system_menu.menu_id,
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
                                                order by system_menu.order_num 
                                                """)
                                        .mapTo(MenuRowMapper.INSTANCE)
                                        .execute(Collections.singletonMap("loginId", username))
                                        .flatMap(rows -> {
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
                                            return Future.succeededFuture(VueRouterUtil.buildVueRouter(vueRouters));
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
    public Future<User> createUser(UserAo userAo) {
        return validateUsername(userAo.getUsername(), userAo.getUserId()).compose(validateUsername -> {
            if (validateUsername) {
                return Future.failedFuture("用户名已存在");
            } else {
                return validateMobile(userAo.getMobile(), userAo.getUserId()).compose(validateMobile -> {
                    if (validateMobile) {
                        return Future.failedFuture("手机号已存在");
                    } else {
                        userAo.setAvatar(CommonCore.DEFAULT_AVATAR);
                        userAo.setPassword(BCrypt.hashpw(CommonCore.DEFAULT_PASSWORD));
                        User user = User.fromAo(userAo);
                        SqlAndParams addUser = sqlStatement().insertNonEmptySQL(user);
                        return pool.withTransaction(client -> client.preparedQuery(addUser.getSql())
                                .execute(addUser.getParams())
                                .flatMap(addUserRes -> {
                                    long userId = addUserRes.property(MySQLClient.LAST_INSERTED_ID);
                                    user.setUserId(userId);
                                    List<UserRole> userRoles = getUserRoles(user, userAo.getRoleIds());
                                    List<UserDataPermission> userDataPermissions = getUserDataPermissions(user, userAo.getDeptIds());
                                    SqlAndParams deleteUserRoles = userRoleService.sqlStatement().deleteByAssistSQL(new SqlAssist().andEq("user_id", user.getUserId()));
                                    SqlAndParams addUserRoles = userRoleService.sqlStatement().insertBatchSQL(userRoles);
                                    SqlAndParams deleteUserDataPermissions = userDataPermissionService.sqlStatement().deleteByAssistSQL(new SqlAssist().andEq("user_id", user.getUserId()));
                                    SqlAndParams addUserDataPermissions = userDataPermissionService.sqlStatement().insertBatchSQL(userDataPermissions);
                                    return client.preparedQuery(deleteUserRoles.getSql())
                                            .execute(deleteUserRoles.getParams())
                                            .flatMap(deleteUserRolesRes -> client.preparedQuery(addUserRoles.getSql())
                                                    .executeBatch(addUserRoles.getBatchParams())
                                                    .flatMap(addUserRolesRes -> client.preparedQuery(deleteUserDataPermissions.getSql())
                                                            .execute(deleteUserDataPermissions.getParams())
                                                            .flatMap(deleteUserDataPermissionsRes -> client.preparedQuery(addUserDataPermissions.getSql())
                                                                    .executeBatch(addUserDataPermissions.getBatchParams())
                                                                    .flatMap(addUserDataPermissionsRes -> deleteUserRelatedCache(user.getUsername())
                                                                            .flatMap(userRelatedCacheRes -> Future.succeededFuture(user))
                                                                    )
                                                            )
                                                    )
                                            );
                                }));
                    }
                });
            }
        });
    }

    @Override
    public Future<User> updateUser(UserAo userAo) {
        return validateUsername(userAo.getUsername(), userAo.getUserId()).compose(validateUsername -> {
            if (validateUsername) {
                return Future.failedFuture("用户名已存在");
            } else {
                return validateMobile(userAo.getMobile(), userAo.getUserId()).compose(validateMobile -> {
                    if (validateMobile) {
                        return Future.failedFuture("手机号已存在");
                    } else {
                        userAo.setAvatar(CommonCore.DEFAULT_AVATAR);
                        userAo.setPassword(null);
                        User user = User.fromAo(userAo);
                        List<UserRole> userRoles = getUserRoles(user, userAo.getRoleIds());
                        List<UserDataPermission> userDataPermissions = getUserDataPermissions(user, userAo.getDeptIds());
                        SqlAndParams updateUser = sqlStatement().updateNonEmptyByIdSQL(user);
                        SqlAndParams deleteUserRoles = userRoleService.sqlStatement().deleteByAssistSQL(new SqlAssist().andEq("user_id", user.getUserId()));
                        SqlAndParams addUserRoles = userRoleService.sqlStatement().insertBatchSQL(userRoles);
                        SqlAndParams deleteUserDataPermissions = userDataPermissionService.sqlStatement().deleteByAssistSQL(new SqlAssist().andEq("user_id", user.getUserId()));
                        SqlAndParams addUserDataPermissions = userDataPermissionService.sqlStatement().insertBatchSQL(userDataPermissions);
                        return pool.withTransaction(client -> client.preparedQuery(updateUser.getSql())
                                .execute(updateUser.getParams())
                                .flatMap(addUserRes -> client.preparedQuery(deleteUserRoles.getSql())
                                        .execute(deleteUserRoles.getParams())
                                        .flatMap(deleteUserRolesRes -> client.preparedQuery(addUserRoles.getSql())
                                                .executeBatch(addUserRoles.getBatchParams())
                                                .flatMap(addUserRolesRes -> client.preparedQuery(deleteUserDataPermissions.getSql())
                                                        .execute(deleteUserDataPermissions.getParams())
                                                        .flatMap(deleteUserDataPermissionsRes -> client.preparedQuery(addUserDataPermissions.getSql())
                                                                .executeBatch(addUserDataPermissions.getBatchParams())
                                                                .flatMap(addUserDataPermissionsRes -> deleteUserRelatedCache(user.getUsername())
                                                                        .flatMap(userRelatedCacheRes -> Future.succeededFuture(user))
                                                                )
                                                        )
                                                )
                                        )
                                ));
                    }
                });
            }
        });
    }

    @Override
    public Future<Void> deleteUser(List<Long> ids) {
        SqlAndParams selectUserSql = sqlStatement().selectAllSQL(new SqlAssist().andIn("user_id", ids));
        SqlAndParams deleteUser = sqlStatement().deleteByIdsSQL(ids);
        SqlAndParams deleteUserRoles = userRoleService.sqlStatement().deleteByAssistSQL(new SqlAssist().andIn("user_id", ids));
        SqlAndParams deleteUserDataPermissions = userDataPermissionService.sqlStatement().deleteByAssistSQL(new SqlAssist().andIn("user_id", ids));
        return pool.withTransaction(client -> client.preparedQuery(selectUserSql.getSql())
                .execute(selectUserSql.getParams())
                .flatMap(selectUserRes -> client.preparedQuery(deleteUser.getSql())
                        .execute(deleteUser.getParams())
                        .flatMap(deleteUserRes -> client.preparedQuery(deleteUserRoles.getSql())
                                .execute(deleteUserRoles.getParams())
                                .flatMap(deleteUserRolesRes -> client.preparedQuery(deleteUserDataPermissions.getSql())
                                        .execute(deleteUserDataPermissions.getParams())
                                        .flatMap(deleteUserDataPermissionsRes -> {
                                            List<String> usernames = new ArrayList<>();
                                            selectUserRes.forEach(row -> {
                                                String userName = row.getString("username");
                                                usernames.add(userName);
                                            });
                                            return deleteUserRelatedCache(usernames.toArray(new String[0]))
                                                    .flatMap(userRelatedCacheRes -> Future.succeededFuture());
                                        })
                                )
                        )
                )
        );
    }

    @Override
    public Future<Boolean> validateUsername(String username, Long userId) {
        return findByName(username).compose(user -> {
            if (user != null && !user.getUserId().equals(userId)) {
                return Future.succeededFuture(true);
            } else {
                return Future.succeededFuture(false);
            }
        });
    }

    @Override
    public Future<Boolean> validateMobile(String mobile, Long userId) {
        return findByMobile(mobile).compose(user -> {
            if (user != null && !user.getUserId().equals(userId)) {
                return Future.succeededFuture(true);
            } else {
                return Future.succeededFuture(false);
            }
        });
    }

    @Override
    public Future<Boolean> deleteUserRelatedCache(String... usernames) {
        List<String> deletePrefix = new ArrayList<>();
        for (String username : usernames) {
            deletePrefix.addAll(List.of(
                    String.format(RedisPrefix.USER_PREFIX, username),
                    String.format(RedisPrefix.USER_MOBILE_PREFIX, username),
                    String.format(RedisPrefix.USER_PERMISSION_PREFIX, username),
                    String.format(RedisPrefix.USER_ROUTER_PREFIX, username)
            ));
        }
        return redis.del(deletePrefix)
                .map(v -> true);
    }

    @Override
    public Future<UserVo> getUserOne(UserVo userVo) {
        return this.getOneByCustomSqlAs(userVoBaseSql(), userVoSqlAssist(userVo), UserVoRowMapper.INSTANCE)
                .compose(one -> setDict(one)
                        .compose(v -> Future.succeededFuture(one))
                )
                ;
    }

    @Override
    public Future<List<UserVo>> getUserList(UserVo userVo) {
        return this.listByCustomSqlAs(userVoBaseSql(), userVoSqlAssist(userVo), UserVoRowMapper.INSTANCE)
                .compose(list -> setDict(list)
                        .compose(v -> Future.succeededFuture(list))
                )
                ;
    }

    @Override
    public Future<Page<UserVo>> getUserPage(UserVo userVo, PageAo pageAo) {
        return this.pageByCustomSqlAs(userVoBaseSql(), userVoSqlAssist(userVo)
                        .page(pageAo), UserVoRowMapper.INSTANCE)
                .compose(page -> setDict(page.getList())
                        .compose(v -> Future.succeededFuture(page))
                )
                ;
    }

    private Future<CompositeFuture> setDict(List<UserVo> list) {
        List<Future<Void>> futures = new ArrayList<>();
        list.forEach(userVo -> futures.addAll(getDictFutures(userVo)));
        return Future.all(futures);
    }

    private Future<CompositeFuture> setDict(UserVo userVo) {
        return Future.all(getDictFutures(userVo));
    }

    private List<Future<Void>> getDictFutures(UserVo userVo) {
        List<Future<Void>> futures = new ArrayList<>();
        futures.add(dictItemService.getDictTitle(DictName.SYSTEM_USER_STATUS, userVo.getStatus())
                .compose(title -> {
                    userVo.setStatusText(title);
                    return Future.succeededFuture();
                }));
        futures.add(dictItemService.getDictTitle(DictName.SYSTEM_USER_GENDER, userVo.getGender())
                .compose(title -> {
                    userVo.setGenderText(title);
                    return Future.succeededFuture();
                }));
        futures.add(dictItemService.getDictTitle(DictName.YES_NO, userVo.getIsTab())
                .compose(title -> {
                    userVo.setIsTabText(title);
                    return Future.succeededFuture();
                }));
        return futures;
    }

    private List<UserRole> getUserRoles(User user, List<Long> roleIds) {
        List<UserRole> userRoles = new ArrayList<>();
        roleIds.stream().filter(Objects::nonNull).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        return userRoles;
    }

    private List<UserDataPermission> getUserDataPermissions(User user, List<Long> deptIds) {
        List<UserDataPermission> userDataPermissions = new ArrayList<>();
        deptIds.stream().filter(Objects::nonNull).forEach(deptId -> {
            UserDataPermission permission = new UserDataPermission();
            permission.setUserId(user.getUserId());
            permission.setDeptId(deptId);
            userDataPermissions.add(permission);
        });
        return userDataPermissions;
    }

    private String userVoBaseSql() {
        return """
                SELECT
                	su.user_id,
                	su.username,
                	su.real_name,
                	su.password,
                	su.dept_id,
                	su.email,
                	su.mobile,
                	su.status,
                	su.last_login_time,
                	su.gender,
                	su.is_tab,
                	su.theme,
                	su.avatar,
                	su.description,
                	su.create_at,
                	GROUP_CONCAT( distinct sr.role_id ) role_id_string,
                	GROUP_CONCAT( distinct sr.role_name ) role_name_string,
                	GROUP_CONCAT( distinct sudp.dept_id ) dept_id_string
                FROM
                	system_user su
                	LEFT JOIN system_user_role sur ON su.user_id = sur.user_id
                	LEFT JOIN system_role sr ON sur.role_id = sr.role_id
                	LEFT JOIN system_user_data_permission sudp ON su.user_id = sudp.user_id
                """;
    }

    private SqlAssist userVoSqlAssist(UserVo userVo) {
        return new SqlAssist()
                .andEq("su.user_id", userVo.getUserId(), If::notNull)
                .andLike("su.username", userVo.getUsername(), If::hasText)
                .setGroupBy("""
                            su.user_id,
                        	su.username,
                        	su.real_name,
                        	su.PASSWORD,
                        	su.dept_id,
                        	su.email,
                        	su.mobile,
                        	su.STATUS,
                        	su.last_login_time,
                        	su.gender,
                        	su.is_tab,
                        	su.theme,
                        	su.avatar,
                        	su.description,
                        	su.create_at
                        """);
    }

}
