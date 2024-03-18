package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.entity.UserRole;
import com.zclcs.platform.system.dao.entity.UserRoleRowMapper;
import com.zclcs.platform.system.service.UserRoleService;
import com.zclcs.sql.helper.service.impl.BaseSqlService;
import io.vertx.sqlclient.Pool;

public class UserRoleServiceImpl extends BaseSqlService<UserRole> implements UserRoleService {

    public UserRoleServiceImpl(Pool pool) {
        super(pool, UserRoleRowMapper.INSTANCE, UserRole.class);
    }

}
