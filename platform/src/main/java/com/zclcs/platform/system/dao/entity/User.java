package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import com.zclcs.platform.system.dao.ao.UserAo;
import com.zclcs.sql.helper.annotation.Table;
import com.zclcs.sql.helper.annotation.TableColumn;
import com.zclcs.sql.helper.annotation.TableId;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户 Entity
 *
 * @author zclcs
 * @since 2023-09-01 19:55:21.249
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
@Table(name = "system_user")
public class User extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableColumn(name = "username")
    private String username;

    /**
     * 用户昵称
     */
    @TableColumn(name = "real_name")
    private String realName;

    /**
     * 密码
     */
    @TableColumn(name = "password")
    private String password;

    /**
     * 部门id
     */
    @TableColumn(name = "dept_id")
    private Long deptId;

    /**
     * 邮箱
     */
    @TableColumn(name = "email")
    private String email;

    /**
     * 联系电话
     */
    @TableColumn(name = "mobile")
    private String mobile;

    /**
     * 状态 @@system_user.status
     */
    @TableColumn(name = "status")
    private String status;

    /**
     * 最近访问时间
     */
    @TableColumn(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 性别 @@system_user.gender
     */
    @TableColumn(name = "gender")
    private String gender;

    /**
     * 是否开启tab @@yes_no
     */
    @TableColumn(name = "is_tab")
    private String isTab;

    /**
     * 主题
     */
    @TableColumn(name = "theme")
    private String theme;

    /**
     * 头像
     */
    @TableColumn(name = "avatar")
    private String avatar;

    /**
     * 描述
     */
    @TableColumn(name = "description")
    private String description;

    public User() {
    }

    public static User fromAo(UserAo userAo) {
        User user = new User();
        user.setUserId(userAo.getUserId());
        user.setUsername(userAo.getUsername());
        user.setRealName(userAo.getRealName());
        user.setPassword(userAo.getPassword());
        user.setDeptId(userAo.getDeptId());
        user.setEmail(userAo.getEmail());
        user.setMobile(userAo.getMobile());
        user.setStatus(userAo.getStatus());
        user.setLastLoginTime(userAo.getLastLoginTime());
        user.setGender(userAo.getGender());
        user.setIsTab(userAo.getIsTab());
        user.setTheme(userAo.getTheme());
        user.setAvatar(userAo.getAvatar());
        user.setDescription(userAo.getDescription());
        return user;
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

}