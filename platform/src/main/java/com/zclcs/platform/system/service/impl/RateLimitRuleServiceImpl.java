package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.entity.RateLimitRule;
import com.zclcs.platform.system.dao.entity.RateLimitRuleRowMapper;
import com.zclcs.platform.system.service.RateLimitRuleService;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RateLimitRuleServiceImpl implements RateLimitRuleService {

    private final SqlClient sqlClient;

    public RateLimitRuleServiceImpl(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public Future<List<RateLimitRule>> getRateEnableLimitRule() {
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT 
                             `rate_limit_rule_id`, 
                             `request_uri`, 
                             `request_method`, 
                             `limit_from`, 
                             `limit_to`, 
                             `rate_limit_count`, 
                             `interval_sec`, 
                             `rule_status`
                        FROM system_rate_limit_rule 
                            where rule_status = "1"
                        """)
                .mapTo(RateLimitRuleRowMapper.INSTANCE)
                .execute(Collections.emptyMap())
                .flatMap(rows -> {
                    List<RateLimitRule> rateLimitRules = new ArrayList<>();
                    rows.forEach(rateLimitRules::add);
                    return Future.succeededFuture(rateLimitRules);
                });
    }

    @Override
    public Future<List<RateLimitRule>> getRateEnableLimitRuleCache() {
        return null;
    }


}
