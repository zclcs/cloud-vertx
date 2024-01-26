package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.bean.Tree;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.cache.DictItemCacheVo;
import com.zclcs.platform.system.dao.cache.DictItemCacheVoRowMapper;
import com.zclcs.platform.system.dao.vo.DictItemTreeVo;
import com.zclcs.platform.system.dao.vo.DictItemVo;
import com.zclcs.platform.system.service.DictItemService;
import io.vertx.core.Future;
import io.vertx.redis.client.RedisAPI;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.time.Duration;
import java.util.*;

/**
 * @author zclcs
 */
public class DictItemServiceImpl implements DictItemService {

    private final SqlClient sqlClient;
    private final RedisAPI redis;

    private final Duration redisExpire = Duration.ofDays(1);

    public DictItemServiceImpl(SqlClient sqlClient, RedisAPI redis) {
        this.sqlClient = sqlClient;
        this.redis = redis;
    }

    private final LocalCache<String, List<DictItemCacheVo>> dictCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    @Override
    public Future<List<Tree<DictItemTreeVo>>> tree(String dictName) {
        return null;
    }

    @Override
    public Future<List<Tree<DictItemTreeVo>>> findDictItemTree(DictItemVo dictItemVo) {
        return null;
    }

    @Override
    public Future<List<DictItemCacheVo>> findByDictName(String dictName) {
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT `id`,
                        `dict_name`,
                        `parent_value`,
                        `value`,
                        `title`,
                        `type`,
                        `whether_system_dict`,
                        `description`,
                        `sorted`,
                        `is_disabled`
                        FROM `system_dict_item`
                        WHERE `dict_name` = #{dictName}
                        """)
                .mapTo(DictItemCacheVoRowMapper.INSTANCE)
                .execute(Collections.singletonMap("dictName", dictName))
                .flatMap(rows -> {
                    List<DictItemCacheVo> dictItemCacheVos = new ArrayList<>();
                    if (rows.size() > 0) {
                        rows.forEach(dictItemCacheVos::add);
                    }
                    return Future.succeededFuture(dictItemCacheVos);
                })
                ;
    }

    @Override
    public Future<List<DictItemCacheVo>> findByDictNameCache(String dictName) {
        String key = String.format(RedisPrefix.DICT_PREFIX, dictName);
        return dictCache.getIfPresent(key).compose(lv -> {
                    if (lv != null) {
                        return Future.succeededFuture(lv);
                    } else {
                        return redis.get(key).compose(rv -> {
                            if (rv != null) {
                                List<DictItemCacheVo> dictItemCacheVos = JsonUtil.readList(rv.toString(), DictItemCacheVo.class);
                                dictCache.put(key, dictItemCacheVos);
                                return Future.succeededFuture(dictItemCacheVos);
                            } else {
                                return findByDictName(dictName).compose(dictItemCacheVos -> {
                                    String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                                    redis.set(List.of(key, JsonUtil.toJson(dictItemCacheVos), "EX", expireTime));
                                    return Future.succeededFuture(dictItemCacheVos);
                                });
                            }
                        });
                    }
                }
        );
    }

    @Override
    public Future<Void> validateDictNameAndValueAndType(String dictName, String value, Long id) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("dictName", dictName);
        params.put("value", value);
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT id FROM `system_dict_item`
                        WHERE `dict_name` = #{dictName}
                        AND `value` = #{value}
                        """)
                .mapTo(Long.class)
                .execute(params)
                .flatMap(rows -> {
                    if (rows.iterator().hasNext()) {
                        Long dictId = rows.iterator().next();
                        if (id.equals(dictId)) {
                            return Future.failedFuture("数据已存在");
                        }
                    }
                    return Future.succeededFuture();
                });
    }
}
