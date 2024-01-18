package com.zclcs.platform.system.dao.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import com.zclcs.platform.system.dao.entity.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户 Vo
 *
 * @author zclcs
 * @since 2023-09-01 19:55:21.249
 */
public class UserVo extends BaseEntity implements Serializable {

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
     * 状态 @@system_user.status
     */
    private String statusText;

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
     * 性别 @@system_user.gender
     */
    private String genderText;

    /**
     * 是否开启tab @@yes_no
     * 默认值：
     */
    private String isTab;

    /**
     * 是否开启tab @@yes_no
     */
    private String isTabText;

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

    /**
     * 用户角色名称集合
     */
    private List<String> roleNames;

    /**
     * 用户角色名称集合String
     */
    private String roleNameString;

    /**
     * 用户权限集合
     */
    private List<String> permissions;


    public UserVo() {
    }

    public UserVo(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.realName = user.getRealName();
            this.password = user.getPassword();
            this.deptId = user.getDeptId();
            this.email = user.getEmail();
            this.mobile = user.getMobile();
            this.status = user.getStatus();
            this.lastLoginTime = user.getLastLoginTime();
            this.gender = user.getGender();
            this.theme = user.getTheme();
            this.avatar = user.getAvatar();
            this.description = user.getDescription();
            this.isTab = user.getIsTab();
        }
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

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
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

    public String getGenderText() {
        return genderText;
    }

    public void setGenderText(String genderText) {
        this.genderText = genderText;
    }

    public String getIsTab() {
        return isTab;
    }

    public void setIsTab(String isTab) {
        this.isTab = isTab;
    }

    public String getIsTabText() {
        return isTabText;
    }

    public void setIsTabText(String isTabText) {
        this.isTabText = isTabText;
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

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public String getRoleNameString() {
        return roleNameString;
    }

    public void setRoleNameString(String roleNameString) {
        this.roleNameString = roleNameString;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}