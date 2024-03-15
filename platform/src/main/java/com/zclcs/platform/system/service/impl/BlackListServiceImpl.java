package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.bean.HttpBlackList;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.entity.BlackList;
import com.zclcs.platform.system.dao.entity.BlackListRowMapper;
import com.zclcs.platform.system.service.BlackListService;
import com.zclcs.sql.helper.service.impl.BaseSqlService;
import com.zclcs.sql.helper.statement.bean.SqlAssist;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.Pool;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zclcs
 */
public class BlackListServiceImpl extends BaseSqlService<BlackList> implements BlackListService {

    private final RedisAPI redis;

    private final Duration redisExpire = Duration.ofDays(1);

    private final Pool pool;

    public BlackListServiceImpl(RedisAPI redis, Pool pool) {
        super(pool, BlackListRowMapper.INSTANCE, BlackList.class);
        this.redis = redis;
        this.pool = pool;
    }

    private final LocalCache<String, List<HttpBlackList>> blackListCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    @Override
    public Future<List<BlackList>> getEnableBlackList() {
        return list(new SqlAssist().andEq("black_status", "1"));
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
