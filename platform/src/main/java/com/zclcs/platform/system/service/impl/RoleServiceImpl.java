package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.entity.Role;
import com.zclcs.platform.system.dao.entity.RoleRowMapper;
import com.zclcs.platform.system.service.RoleService;
import com.zclcs.sql.helper.service.impl.BaseSqlService;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zclcs
 */
public class RoleServiceImpl extends BaseSqlService<Role> implements RoleService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Pool pool;

    public RoleServiceImpl(Pool pool) {
        super(pool, RoleRowMapper.INSTANCE, Role.class);
        this.pool = pool;
    }

    @Override
    public Future<List<Role>> getRolesByUserId(Long userId) {
        return SqlTemplate.forQuery(pool, """
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

    @Override
    public Future<List<Role>> getRoleOptions() {
        return SqlTemplate.forQuery(pool, """
                        SELECT `role_id`, `role_name`, `role_code`, `remark`, `create_at` 
                        FROM `system_role` ORDER BY `create_at` DESC
                        """)
                .mapTo(RoleRowMapper.INSTANCE)
                .execute(Collections.emptyMap())
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
