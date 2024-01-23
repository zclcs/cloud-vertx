package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;

/**
 * 黑名单 Entity
 *
 * @author zclcs
 * @since 2023-09-01 19:53:59.035
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
public class BlackList extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 黑名单id
     */
    @Column(name = "black_id")
    private Long blackId;

    /**
     * 黑名单ip
     */
    @Column(name = "black_ip")
    private String blackIp;

    /**
     * 请求uri（支持通配符）
     */
    @Column(name = "request_uri")
    private String requestUri;

    /**
     * 请求方法，如果为ALL则表示对所有方法生效
     */
    @Column(name = "request_method")
    private String requestMethod;

    /**
     * 限制时间起
     */
    @Column(name = "limit_from")
    private String limitFrom;

    /**
     * 限制时间止
     */
    @Column(name = "limit_to")
    private String limitTo;

    /**
     * ip对应地址
     */
    @Column(name = "location")
    private String location;

    /**
     * 黑名单状态 默认 1 @@enable_disable
     */
    @Column(name = "black_status")
    private String blackStatus;

    public BlackList() {
    }

    public HttpBlackList toHttpBlackList() {
        return new HttpBlackList(blackIp, requestMethod, requestUri, limitFrom, limitTo);
    }

    public Long getBlackId() {
        return blackId;
    }

    public void setBlackId(Long blackId) {
        this.blackId = blackId;
    }

    public String getBlackIp() {
        return blackIp;
    }

    public void setBlackIp(String blackIp) {
        this.blackIp = blackIp;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getLimitFrom() {
        return limitFrom;
    }

    public void setLimitFrom(String limitFrom) {
        this.limitFrom = limitFrom;
    }

    public String getLimitTo() {
        return limitTo;
    }

    public void setLimitTo(String limitTo) {
        this.limitTo = limitTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBlackStatus() {
        return blackStatus;
    }

    public void setBlackStatus(String blackStatus) {
        this.blackStatus = blackStatus;
    }
}