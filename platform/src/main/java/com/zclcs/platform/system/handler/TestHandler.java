package com.zclcs.platform.system.handler;

import com.zclcs.common.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.dao.entity.User;
import com.zclcs.platform.system.dao.entity.UserRowMapper;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author zclcs
 */
public class TestHandler implements Handler<RoutingContext> {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private final Pool dbClient;

    public TestHandler(Pool mysqlClient) {
        this.dbClient = mysqlClient;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String username = ctx.request().getParam("username");
        getUser(username).timeout(1, TimeUnit.SECONDS)
                .onComplete(ar -> {
                    User user = ar.result();
                    if (user != null) {
                        RoutingContextUtil.success(ctx, Json.encode(user));
                    } else {
                        RoutingContextUtil.success(ctx, "用户名或密码错误");
                    }
                });
    }

    private Future<User> getUser(String loginId) {
        return SqlTemplate.forQuery(dbClient, """
                        SELECT * FROM system_user WHERE username = #{loginId}
                        """)
                .mapTo(UserRowMapper.INSTANCE)
                .execute(Collections.singletonMap("loginId", loginId))
                .map(rs -> {
                    if (rs.iterator().hasNext()) {
                        return rs.iterator().next();
                    } else {
                        return null;
                    }
                });
    }

}