package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.entity.Role;
import com.zclcs.platform.system.dao.entity.RoleRowMapper;
import com.zclcs.platform.system.service.RoleService;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SqlClient sqlClient;

    public RoleServiceImpl(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public Future<List<Role>> getRolesByUserId(Long userId) {
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT * FROM role WHERE user_id = #{userId}
                        """)
                .mapTo(RoleRowMapper.INSTANCE)
                .execute(Collections.singletonMap("userId", userId))
                .flatMap(rows -> {
                    List<Role> roles = null;
                    if (rows != null && rows.size() > 0) {
                        roles = new ArrayList<>();
                        for (Role role : rows) {
                            roles.add(role);
                        }
                    }
                    return Future.succeededFuture(roles);
                })
                ;
    }

}
