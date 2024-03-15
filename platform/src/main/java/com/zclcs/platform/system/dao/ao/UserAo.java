package com.zclcs.platform.system.dao.ao;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户 Ao
 *
 * @author zclcs
 * @since 2023-09-01 19:55:21.249
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
public class UserAo implements Serializable {

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
     * 默认值：默认密码
     */
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

    /**
     * 角色id集合
     */
    private List<Long> roleIds;

    /**
     * 数据权限id集合
     */
    private List<Long> deptIds;

    public UserAo() {
    }

    public UserAo(Long userId, String username, String realName, String password, Long deptId, String email, String mobile, String status, LocalDateTime lastLoginTime, String gender, String isTab, String theme, String avatar, String description, List<Long> roleIds, List<Long> deptIds) {
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.password = password;
        this.deptId = deptId;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
        this.lastLoginTime = lastLoginTime;
        this.gender = gender;
        this.isTab = isTab;
        this.theme = theme;
        this.avatar = avatar;
        this.description = description;
        this.roleIds = roleIds;
        this.deptIds = deptIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIsTab() {
        return isTab;
    }

    public void setIsTab(String isTab) {
        this.isTab = isTab;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }
}