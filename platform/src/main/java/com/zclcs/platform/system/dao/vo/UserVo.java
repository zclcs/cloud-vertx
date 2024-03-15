package com.zclcs.platform.system.dao.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import com.zclcs.common.core.constant.StringPool;
import com.zclcs.common.core.utils.StringsUtil;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 Vo
 *
 * @author zclcs
 * @since 2023-09-01 19:55:21.249
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
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
     * 用户角色名称集合String
     */
    private String roleIdString;

    /**
     * 数据权限id集合
     */
    private List<Long> deptIds;

    /**
     * 数据权限id集合String
     */
    private String deptIdString;

    /**
     * 用户角色名称集合
     */
    private List<String> roleNames;

    /**
     * 用户角色名称集合String
     */
    private String roleNameString;


    public UserVo() {
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

    public String getRoleIdString() {
        return roleIdString;
    }

    public void setRoleIdString(String roleIdString) {
        this.roleIdString = roleIdString;
        if (StringsUtil.isNotBlank(roleIdString)) {
            this.roleIds = Arrays.stream(roleIdString.split(StringPool.COMMA)).map(Long::valueOf).collect(Collectors.toList());
        }
    }

    public List<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public String getDeptIdString() {
        return deptIdString;
    }

    public void setDeptIdString(String deptIdString) {
        this.deptIdString = deptIdString;
        if (StringsUtil.isNotBlank(deptIdString)) {
            this.deptIds = Arrays.stream(deptIdString.split(StringPool.COMMA)).map(Long::valueOf).collect(Collectors.toList());
        }
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
        if (StringsUtil.isNotBlank(roleNameString)) {
            this.roleNames = Arrays.stream(roleNameString.split(StringPool.COMMA)).collect(Collectors.toList());
        }
    }
}