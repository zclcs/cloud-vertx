package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.entity.User;
import com.zclcs.platform.system.dao.entity.UserRowMapper;
import com.zclcs.platform.system.service.UserService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.Collections;

public class UserServiceImpl implements UserService {

    private final Pool dbClient;

    public UserServiceImpl(Pool dbClient) {
        this.dbClient = dbClient;
    }

    @Override
    public UserService getUser(String username, Handler<AsyncResult<User>> handler) {
        SqlTemplate.forQuery(dbClient, """
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
                }).onComplete(handler);
        return this;
    }
}
