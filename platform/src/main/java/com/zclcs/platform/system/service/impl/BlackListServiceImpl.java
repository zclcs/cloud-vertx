package com.zclcs.platform.system.service.impl;

import com.zclcs.platform.system.dao.entity.BlackList;
import com.zclcs.platform.system.dao.entity.BlackListRowMapper;
import com.zclcs.platform.system.service.BlackListService;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zclcs
 */
public class BlackListServiceImpl implements BlackListService {

    private final SqlClient sqlClient;

    public BlackListServiceImpl(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public Future<List<BlackList>> getEnableBlackList() {
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT 
                            `black_id`, 
                            `black_ip`, 
                            `request_uri`, 
                            `request_method`, 
                            `limit_from`, 
                            `limit_to`, 
                            `location`, 
                            `black_status` 
                        FROM system_black_list 
                        """)
                .mapTo(BlackListRowMapper.INSTANCE)
                .execute(Collections.emptyMap())
                .flatMap(rows -> {
                    List<BlackList> blackLists = new ArrayList<>();
                    if (rows.size() > 0) {
                        rows.forEach(blackLists::add);
                    }
                    return Future.succeededFuture(blackLists);
                })
                ;
    }

    @Override
    public Future<List<BlackList>> getEnableBlackListCache() {
        return null;
    }
}
