package com.zclcs.platform.system.service;

import com.zclcs.platform.system.dao.entity.Role;
import com.zclcs.sql.helper.service.SqlService;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface RoleService extends SqlService<Role> {

    /**
     * 根据用户id获取角色
     *
     * @param userId 用户id
     * @return 角色集合
     */
    Future<List<Role>> getRolesByUserId(Long userId);

    /**
     * 获取角色选项
     *
     * @return 角色选项集合
     */
    Future<List<Role>> getRoleOptions();

}
