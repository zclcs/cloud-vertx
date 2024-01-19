package com.zclcs.platform.system.dao.vo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门树
 *
 * @author zclcs
 */
public class DeptTreeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否顶级部门
     */
    private Boolean harPar;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 排序
     */
    private Double orderNum;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    public DeptTreeVo() {
    }

    public Boolean getHarPar() {
        return harPar;
    }

    public void setHarPar(Boolean harPar) {
        this.harPar = harPar;
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
