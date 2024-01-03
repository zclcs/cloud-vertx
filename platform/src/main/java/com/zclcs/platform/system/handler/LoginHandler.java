package com.zclcs.platform.system.handler;

import com.zclcs.common.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.entity.User;
import com.zclcs.platform.system.entity.UserRowMapper;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.Collections;
import java.util.UUID;

public class LoginHandler implements Handler<RoutingContext> {

    private final Pool dbClient;

    public LoginHandler(Pool mysqlClient) {
        this.dbClient = mysqlClient;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String loginId = ctx.request().getParam("loginId");
        SqlTemplate.forQuery(dbClient, """
                        SELECT * FROM user WHERE username = #{loginId} OR mobile = #{loginId}
                        """)
                .mapTo(UserRowMapper.INSTANCE)
                .execute(Collections.singletonMap("loginId", loginId))
                .onSuccess(rs -> {
                    if (rs.iterator().hasNext()) {
                        User user = rs.iterator().next();
                        if ("1".equals(user.getStatus())) {
                            RoutingContextUtil.success(ctx, UUID.randomUUID().toString().replace("-", ""));
                        }
                    } else {
                        RoutingContextUtil.success(ctx, "用户不存在");
                    }
                });
    }
}
