package com.zclcs.platform.system.dao.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zclcs.platform.system.dao.cache.UserCacheVo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录 Vo
 *
 * @author zclcs
 * @since 2023-01-10 10:39:34.182
 */
public class LoginVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     * 默认值：
     */
    private Long userId;

    /**
     * 用户名
     * 默认值：
     */
    private String username;

    /**
     * 用户昵称
     * 默认值：
     */
    private String realName;

    /**
     * 密码
     * 默认值：
     */
    @JsonIgnore
    private String password;

    /**
     * 部门id
     * 默认值：0
     */
    private Long deptId;

    /**
     * 邮箱
     * 默认值：
     */
    private String email;

    /**
     * 联系电话
     * 默认值：
     */
    private String mobile;

    /**
     * 状态 @@system_user.status
     * 默认值：1
     */
    private String status;

    /**
     * 最近访问时间
     * 默认值：CURRENT_TIMESTAMP
     */
    private LocalDateTime lastLoginTime;

    /**
     * 性别 @@system_user.gender
     * 默认值：
     */
    private String gender;

    /**
     * 是否开启tab @@yes_no
     * 默认值：
     */
    private String isTab;

    /**
     * 主题
     * 默认值：
     */
    private String theme;

    /**
     * 头像
     * 默认值：
     */
    private String avatar;

    /**
     * 描述
     * 默认值：
     */
    private String description;

    public LoginVo() {
    }

    public LoginVo(UserCacheVo userCacheVo) {
        if (userCacheVo != null) {
            this.userId = userCacheVo.getUserId();
            this.username = userCacheVo.getUsername();
            this.realName = userCacheVo.getRealName();
            this.password = userCacheVo.getPassword();
            this.deptId = userCacheVo.getDeptId();
            this.mobile = userCacheVo.getMobile();
            this.email = userCacheVo.getEmail();
            this.status = userCacheVo.getStatus();
            this.lastLoginTime = userCacheVo.getLastLoginTime();
            this.gender = userCacheVo.getGender();
            this.isTab = userCacheVo.getIsTab();
            this.theme = userCacheVo.getTheme();
            this.avatar = userCacheVo.getAvatar();
            this.description = userCacheVo.getDescription();
        }
    }

}