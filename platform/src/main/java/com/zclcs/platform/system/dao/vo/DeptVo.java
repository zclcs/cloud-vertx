package com.zclcs.platform.system.dao.vo;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 部门 Vo
 *
 * @author zclcs
 * @since 2023-09-01 19:53:38.826
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
public class DeptVo extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     * 默认值：
     */
    private Long deptId;

    /**
     * 部门编码
     * 默认值：
     */
    private String deptCode;

    /**
     * 上级部门编码
     * 默认值：0
     */
    private String parentCode;

    /**
     * 部门名称
     * 默认值：
     */
    private String deptName;

    /**
     * 排序
     * 默认值：1
     */
    private Double orderNum;

    /**
     * 创建时间-开始
     */
    private LocalDate createTimeFrom;

    /**
     * 创建时间-结束
     */
    private LocalDate createTimeTo;

    public DeptVo() {
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

    public LocalDate getCreateTimeFrom() {
        return createTimeFrom;
    }

    public void setCreateTimeFrom(LocalDate createTimeFrom) {
        this.createTimeFrom = createTimeFrom;
    }

    public LocalDate getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(LocalDate createTimeTo) {
        this.createTimeTo = createTimeTo;
    }
}