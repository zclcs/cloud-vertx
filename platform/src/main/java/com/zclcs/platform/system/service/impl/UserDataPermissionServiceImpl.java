package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.entity.UserDataPermission;
import com.zclcs.platform.system.dao.entity.UserDataPermissionRowMapper;
import com.zclcs.platform.system.service.UserDataPermissionService;
import com.zclcs.sql.helper.service.impl.BaseSqlService;
import io.vertx.sqlclient.Pool;

public class UserDataPermissionServiceImpl extends BaseSqlService<UserDataPermission> implements UserDataPermissionService {

    public UserDataPermissionServiceImpl(Pool pool) {
        super(pool, UserDataPermissionRowMapper.INSTANCE, UserDataPermission.class);
    }

}
