package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.core.bean.HttpRateLimitList;
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
 * 限流规则 Entity
 *
 * @author zclcs
 * @since 2023-09-01 19:53:43.828
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
@Table(name = "system_rate_limit_rule")
public class RateLimitRule extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 限流规则id
     */
    @TableId(name = "rate_limit_rule_id")
    private Long rateLimitRuleId;

    /**
     * 请求uri（不支持通配符）
     */
    @TableColumn(name = "request_uri")
    private String requestUri;

    /**
     * 请求方法，如果为ALL则表示对所有方法生效
     */
    @TableColumn(name = "request_method")
    private String requestMethod;

    /**
     * 限制时间起
     */
    @TableColumn(name = "limit_from")
    private String limitFrom;

    /**
     * 限制时间止
     */
    @TableColumn(name = "limit_to")
    private String limitTo;

    /**
     * 限制次数
     */
    @TableColumn(name = "rate_limit_count")
    private Integer rateLimitCount;

    /**
     * 时间周期（单位秒）
     */
    @TableColumn(name = "interval_sec")
    private String intervalSec;

    /**
     * 规则状态 默认 1 @@enable_disable
     */
    @TableColumn(name = "rule_status")
    private String ruleStatus;

    public RateLimitRule() {
    }

    public HttpRateLimitList toHttpRateLimitList() {
        return new HttpRateLimitList(requestMethod, requestUri, limitFrom, limitTo, rateLimitCount, Integer.valueOf(intervalSec));
    }

    public Long getRateLimitRuleId() {
        return rateLimitRuleId;
    }

    public void setRateLimitRuleId(Long rateLimitRuleId) {
        this.rateLimitRuleId = rateLimitRuleId;
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

    public Integer getRateLimitCount() {
        return rateLimitCount;
    }

    public void setRateLimitCount(Integer rateLimitCount) {
        this.rateLimitCount = rateLimitCount;
    }

    public String getIntervalSec() {
        return intervalSec;
    }

    public void setIntervalSec(String intervalSec) {
        this.intervalSec = intervalSec;
    }

    public String getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(String ruleStatus) {
        this.ruleStatus = ruleStatus;
    }
}