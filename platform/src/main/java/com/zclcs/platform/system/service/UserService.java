package com.zclcs.platform.system.service;

import com.zclcs.platform.system.dao.ao.UserAo;
import com.zclcs.platform.system.dao.cache.MenuCacheVo;
import com.zclcs.platform.system.dao.cache.UserCacheVo;
import com.zclcs.platform.system.dao.entity.User;
import com.zclcs.platform.system.dao.router.VueRouter;
import com.zclcs.platform.system.dao.vo.UserVo;
import com.zclcs.sql.helper.service.SqlService;
import com.zclcs.sql.helper.statement.bean.Page;
import com.zclcs.sql.helper.statement.bean.PageAo;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * @author zclcs
 */
public interface UserService extends SqlService<User> {

    /**
     * 获取用户信息
     *
     * @param ctx 上下文
     * @return 用户信息
     */
    Future<UserCacheVo> getUserInfo(RoutingContext ctx);

    /**
     * 通过用户名查找用户
     *
     * @param id 用户id
     * @return 用户
     */
    Future<User> findById(Long id);

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    Future<User> findByName(String username);

    /**
     * 通过手机号查找用户
     *
     * @param mobile 手机号
     * @return 用户
     */
    Future<User> findByMobile(String mobile);

    /**
     * 根据用户名获取用户缓存
     *
     * @param username 用户名
     * @return 用户缓存
     */
    Future<UserCacheVo> getUserCacheByName(String username);

    /**
     * 根据手机号获取用户缓存
     *
     * @param mobile 手机号
     * @return 用户缓存
     */
    Future<UserCacheVo> getUserCacheByMobile(String mobile);

    /**
     * 根据用户名获取用户权限
     *
     * @param username 用户名
     * @return 用户权限
     */
    Future<List<String>> getUserPermissionCache(String username);

    /**
     * 根据用户名获取用户路由
     *
     * @param username 用户名
     * @return 用户路由
     */
    Future<List<VueRouter<MenuCacheVo>>> getUserRouterCache(String username);

    /**
     * 新增
     *
     * @param userAo {@link UserAo}
     * @return {@link User}
     */
    Future<UserAo> createUser(UserAo userAo);

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @param userId   用户id
     * @return 存在返回 true 否则 false
     */
    Future<Boolean> validateUsername(String username, Long userId);

    /**
     * 验证手机号
     *
     * @param mobile 手机号
     * @param userId 用户id
     * @return 存在返回 true 否则 false
     */
    Future<Boolean> validateMobile(String mobile, Long userId);

    /**
     * 删除用户相关缓存
     *
     * @param username 用户名
     * @return 成功
     */
    Future<Boolean> deleteUserRelatedCache(String username);

    /**
     * 获取用户分页
     *
     * @param userVo 用户信息
     * @param pageAo 分页信息
     * @return 用户分页
     */
    Future<Page<UserVo>> getUserPage(UserVo userVo, PageAo pageAo);

}
