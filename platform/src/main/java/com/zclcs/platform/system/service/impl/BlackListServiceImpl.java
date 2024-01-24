package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.entity.BlackList;
import com.zclcs.platform.system.dao.entity.BlackListRowMapper;
import com.zclcs.platform.system.service.BlackListService;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author zclcs
 */
public class BlackListServiceImpl implements BlackListService {

    private final RedisAPI redis;

    private final Duration redisExpire = Duration.ofDays(1);

    private final SqlClient sqlClient;

    public BlackListServiceImpl(RedisAPI redis, SqlClient sqlClient) {
        this.redis = redis;
        this.sqlClient = sqlClient;
    }

    private final LocalCache<String, List<HttpBlackList>> blackListCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

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
                            where black_status = '1'
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
    public Future<List<HttpBlackList>> getEnableHttpBlackListCache() {
        return blackListCache.getIfPresent(RedisPrefix.HTTP_BLACK_LIST_PREFIX).compose(blackLists -> {
            if (blackLists != null) {
                return Future.succeededFuture(blackLists);
            } else {
                return redis.get(RedisPrefix.HTTP_BLACK_LIST_PREFIX).compose(response -> {
                    if (response != null) {
                        List<HttpBlackList> redisValue = JsonUtil.readList(response.toString(), HttpBlackList.class);
                        blackListCache.put(RedisPrefix.HTTP_BLACK_LIST_PREFIX, redisValue);
                        return Future.succeededFuture(redisValue);
                    } else {
                        return getEnableBlackList().compose(dv -> {
                            String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                            List<HttpBlackList> httpBlackLists = new ArrayList<>();
                            if (!dv.isEmpty()) {
                                dv.forEach(blackList -> httpBlackLists.add(blackList.toHttpBlackList()));
                            }
                            redis.set(List.of(RedisPrefix.HTTP_BLACK_LIST_PREFIX, JsonUtil.toJson(httpBlackLists), "EX", expireTime));
                            return Future.succeededFuture(httpBlackLists);
                        });
                    }
                });
            }
        });
    }
}
