package com.zclcs.platform.system.service;

import com.zclcs.platform.system.dao.entity.User;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface UserService {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    Future<User> getUser(String username);

    /**
     * 根据用户名获取用户权限
     *
     * @param username 用户名
     * @return 用户权限
     */
    Future<List<String>> getUserPermission(String username);

}
