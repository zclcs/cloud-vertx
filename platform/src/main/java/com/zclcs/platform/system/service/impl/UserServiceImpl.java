package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.entity.User;
import com.zclcs.platform.system.dao.entity.UserRowMapper;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Pool dbClient;
    private final RedisAPI redis;

    public UserServiceImpl(Pool dbClient, RedisAPI redis) {
        this.dbClient = dbClient;
        this.redis = redis;
    }

    private final LocalCache<String, User> userCache = new LocalCache<>(500, 10, Duration.ofSeconds(5));

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
                                        redis.set(List.of(k, Json.encode(dv)));
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
}
