package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import com.zclcs.sql.helper.annotation.Table;
import com.zclcs.sql.helper.annotation.TableColumn;
import com.zclcs.sql.helper.annotation.TableId;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色 Entity
 *
 * @author zclcs
 * @since 2023-09-01 19:53:33.519
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
@Table(name = "system_role")
public class Role extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(name = "role_id")
    private Long roleId;

    /**
     * 角色编码（唯一值）
     */
    @TableColumn(name = "role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @TableColumn(name = "role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @TableColumn(name = "remark")
    private String remark;

    public Role() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}