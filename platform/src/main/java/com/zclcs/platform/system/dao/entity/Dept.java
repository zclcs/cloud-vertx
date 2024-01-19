package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;

/**
 * 部门 Entity
 *
 * @author zclcs
 * @since 2023-09-01 19:53:38.826
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
public class Dept extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 部门编码
     */
    @Column(name = "dept_code")
    private String deptCode;

    /**
     * 上级部门编码
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Double orderNum;

    public Dept() {
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Double getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Double orderNum) {
        this.orderNum = orderNum;
    }
}