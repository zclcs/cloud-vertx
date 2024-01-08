package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.entity.User;
import com.zclcs.platform.system.dao.entity.UserRowMapper;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zclcs
 */
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Pool dbClient;
    private final RedisAPI redis;

    private final Duration redisTokenExpire = Duration.ofDays(1);

    public UserServiceImpl(Pool dbClient, RedisAPI redis) {
        this.dbClient = dbClient;
        this.redis = redis;
    }

    private final LocalCache<String, User> userCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    private final LocalCache<String, List<String>> permissionCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    @Override
    public Future<User> getUser(String username) {
        String k = String.format(RedisPrefix.USER_PREFIX, username);
        return userCache.getIfPresent(k).compose(v -> {
            if (v != null) {
                return Future.succeededFuture(v);
            } else {
                return redis.get(k).compose(rv -> {
                    if (rv != null) {
                        User user = Json.decodeValue(rv.toString(), User.class);
                        userCache.put(k, user);
                        return Future.succeededFuture(user);
                    } else {
                        return SqlTemplate.forQuery(dbClient, """
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
                                }).compose(dv -> {
                                    if (dv != null) {
                                        String expireTime = String.valueOf(redisTokenExpire.getSeconds() + (long) ((Math.random() * 100) + 1));
                                        redis.set(List.of(k, Json.encode(dv), "EX", expireTime));
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
    public Future<List<String>> getUserPermission(String username) {
        String k = String.format(RedisPrefix.USER_PERMISSION_PREFIX, username);
        return permissionCache.getIfPresent(k).compose(v -> {
            if (v != null) {
                return Future.succeededFuture(v);
            } else {
                return redis.get(k).compose(rv -> {
                    if (rv != null) {
                        List<String> permissions = new ArrayList<>();
                        JsonArray objects = JsonArray.of(rv.toString());
                        if (!objects.isEmpty()) {
                            for (Object object : objects) {
                                permissions.add(object.toString());
                            }
                        }
                        permissionCache.put(k, permissions);
                        return Future.succeededFuture(permissions);
                    } else {
                        return SqlTemplate.forQuery(dbClient, """
                                        SELECT system_menu.perms FROM system_menu 
                                        INNER JOIN system_role_menu ON system_menu.menu_id = system_role_menu.menu_id 
                                        INNER JOIN system_role ON system_role_menu.role_id = system_role.role_id 
                                        INNER JOIN system_user_role ON system_role.role_id = system_user_role.role_id 
                                        INNER JOIN system_user ON system_user_role.user_id = system_user.user_id
                                        WHERE system_user.username = #{loginId} and system_menu.perms != ''
                                        """)
                                .execute(Collections.singletonMap("loginId", username))
                                .map(rs -> {
                                    List<String> permissions = new ArrayList<>();
                                    if (rs.iterator().hasNext()) {
                                        Row next = rs.iterator().next();
                                        permissions.add(next.getString(0));
                                    }
                                    return permissions;
                                }).compose(dv -> {
                                    String expireTime = String.valueOf(redisTokenExpire.getSeconds() + (long) ((Math.random() * 100) + 1));
                                    redis.set(List.of(k, Json.encode(dv), "EX", expireTime));
                                    return Future.succeededFuture(dv);
                                });
                    }
                });
            }
        });
    }
}
