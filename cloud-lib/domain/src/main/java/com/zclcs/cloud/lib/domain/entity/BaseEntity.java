package com.zclcs.cloud.lib.domain.entity;

import io.vertx.sqlclient.templates.annotations.Column;

import java.time.LocalDateTime;

/**
 * 基础实体
 *
 * @author zclcs
 */
public class BaseEntity {

    /**
     * 版本
     */
    @Column(name = "version")
    private Long version;

    /**
     * 租户id
     */
    @Column(name = "tenant_id")
    private String tenantId;

    /**
     * 创建时间
     */
    @Column(name = "create_at")
    private LocalDateTime createAt;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 编辑时间
     */
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    /**
     * 编辑人
     */
    @Column(name = "update_by")
    private String updateBy;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
