package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import com.zclcs.sql.helper.annotation.Table;
import com.zclcs.sql.helper.annotation.TableId;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联 Entity
 *
 * @author zclcs
 * @since 2023-09-01 19:54:58.035
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
@Table(name = "system_user_role")
public class UserRole extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(name = "user_id")
    private Long userId;

    /**
     * 角色id
     */
    @TableId(name = "role_id")
    private Long roleId;

    public UserRole() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}