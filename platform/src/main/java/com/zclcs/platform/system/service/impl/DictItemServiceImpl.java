package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.bean.Tree;
import com.zclcs.cloud.core.constant.RedisPrefix;
import com.zclcs.cloud.core.utils.TreeUtil;
import com.zclcs.common.config.utils.JsonUtil;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.common.local.cache.LocalCache;
import com.zclcs.platform.system.dao.cache.DictItemCacheVo;
import com.zclcs.platform.system.dao.cache.DictItemCacheVoRowMapper;
import com.zclcs.platform.system.dao.vo.DictItemTreeVo;
import com.zclcs.platform.system.dao.vo.DictItemVo;
import com.zclcs.platform.system.dao.vo.DictItemVoRowMapper;
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

    private final LocalCache<String, DictItemCacheVo> dictValueCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));
    private final LocalCache<String, List<DictItemCacheVo>> dictParentValueCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));
    private final LocalCache<String, DictItemCacheVo> dictParentValueAndValueCache = new LocalCache<>(5000, 10, Duration.ofSeconds(5));

    @Override
    public Future<List<Tree<DictItemTreeVo>>> tree(String dictName) {
        String sql = """
                SELECT
                        `id`,
                        `dict_name`,
                        `parent_value`,
                        `value`,
                        `title`,
                        `type`,
                        ( SELECT title FROM system_dict_item tmp WHERE tmp.dict_name = 'system_dict_item.type' AND tmp.`value` = tb.type ) type_text,
                        `whether_system_dict`,
                        ( SELECT title FROM system_dict_item tmp WHERE tmp.dict_name = 'yes_no' AND tmp.`value` = tb.type ) whether_system_dict_text,
                        `description`,
                        `sorted`,
                        `is_disabled`
                FROM
                        `system_dict_item` tb
                where `dict_name` = #{dictName}
                order by sorted
                """;
        return SqlTemplate.forQuery(sqlClient, sql)
                .mapTo(DictItemVoRowMapper.INSTANCE)
                .execute(Collections.singletonMap("dictName", dictName))
                .flatMap(rows -> {
                    List<Tree<DictItemTreeVo>> dictItemCacheVos = new ArrayList<>();
                    if (rows.size() > 0) {
                        rows.forEach(dictItemVo -> {
                            Tree<DictItemTreeVo> dictItemTreeVoTree = new Tree<>();
                            dictItemTreeVoTree.setId(dictItemVo.getId());
                            dictItemTreeVoTree.setCode(dictName);
                            dictItemTreeVoTree.setParentCode(dictItemVo.getParentValue());
                            dictItemTreeVoTree.setLabel(dictItemVo.getTitle());
                            DictItemTreeVo dictItemTreeVo = getDictItemTreeVo(dictItemVo);
                            dictItemTreeVoTree.setExtra(dictItemTreeVo);
                            dictItemCacheVos.add(dictItemTreeVoTree);
                        });
                    }
                    return Future.succeededFuture(TreeUtil.build(dictItemCacheVos));
                })
                ;
    }

    private static DictItemTreeVo getDictItemTreeVo(DictItemVo dictItemVo) {
        DictItemTreeVo dictItemTreeVo = new DictItemTreeVo();
        dictItemTreeVo.setDescription(dictItemVo.getDescription());
        dictItemTreeVo.setType(dictItemVo.getType());
        dictItemTreeVo.setTypeText(dictItemVo.getTypeText());
        dictItemTreeVo.setWhetherSystemDict(dictItemVo.getWhetherSystemDict());
        dictItemTreeVo.setWhetherSystemDictText(dictItemVo.getWhetherSystemDictText());
        dictItemTreeVo.setSorted(dictItemVo.getSorted());
        return dictItemTreeVo;
    }

    @Override
    public Future<List<DictItemCacheVo>> findDictItemCacheVoList(DictItemCacheVo dictItemCacheVo) {
        String sql = """
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
                WHERE 1=1
                """;
        String condition = getDictItemCacheVoCondition(dictItemCacheVo);
        if (StringsUtil.isNotBlank(condition)) {
            sql += condition;
        }
        return SqlTemplate.forQuery(sqlClient, sql)
                .mapTo(DictItemCacheVoRowMapper.INSTANCE)
                .execute(dictItemCacheVo.toMap())
                .flatMap(rows -> {
                    List<DictItemCacheVo> dictItemCacheVos = new ArrayList<>();
                    if (rows.size() > 0) {
                        rows.forEach(dictItemCacheVos::add);
                    }
                    return Future.succeededFuture(dictItemCacheVos);
                })
                ;
    }

    private static String getDictItemCacheVoCondition(DictItemCacheVo dictItemCacheVo) {
        String condition = "";
        if (StringsUtil.isNotBlank(dictItemCacheVo.getDictName())) {
            condition += " and dict_name = #{dictName}";
        }
        if (StringsUtil.isNotBlank(dictItemCacheVo.getParentValue())) {
            condition += " and parent_value = #{parentValue}";
        }
        if (StringsUtil.isNotBlank(dictItemCacheVo.getValue())) {
            condition += " and value = #{value}";
        }
        if (StringsUtil.isNotBlank(dictItemCacheVo.getTitle())) {
            condition += " and title = #{title}";
        }
        return condition;
    }

    @Override
    public Future<List<DictItemCacheVo>> findDictCacheByDictName(String dictName) {
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
                                return findDictItemCacheVoList(new DictItemCacheVo(dictName)).compose(dictItemCacheVos -> {
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
    public Future<DictItemCacheVo> findDictCacheByDictNameAndValue(String dictName, String value) {
        if (StringsUtil.isBlank(dictName) || StringsUtil.isBlank(value)) {
            return Future.succeededFuture(null);
        }
        String key = String.format(RedisPrefix.DICT_VALUE_PREFIX, dictName, value);
        return dictValueCache.getIfPresent(key).compose(lv -> {
                    if (lv != null) {
                        return Future.succeededFuture(lv);
                    } else {
                        return redis.get(key).compose(rv -> {
                            if (rv != null) {
                                DictItemCacheVo dictItemCacheVo = JsonUtil.readValue(rv.toString(), DictItemCacheVo.class);
                                dictValueCache.put(key, dictItemCacheVo);
                                return Future.succeededFuture(dictItemCacheVo);
                            } else {
                                return findDictItemCacheVoList(new DictItemCacheVo(dictName, value))
                                        .flatMap(dictItemCacheVos -> {
                                                    if (dictItemCacheVos.isEmpty()) {
                                                        return Future.succeededFuture(new DictItemCacheVo());
                                                    } else if (dictItemCacheVos.size() == 1) {
                                                        return Future.succeededFuture(dictItemCacheVos.get(0));
                                                    } else {
                                                        return Future.failedFuture("find dict cache by dictName and value more than one");
                                                    }
                                                }
                                        ).compose(dictItemCacheVo -> {
                                            String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                                            redis.set(List.of(key, JsonUtil.toJson(dictItemCacheVo), "EX", expireTime));
                                            return Future.succeededFuture(dictItemCacheVo);
                                        });
                            }
                        });
                    }
                }
        );
    }

    @Override
    public Future<List<DictItemCacheVo>> findDictCacheByDictNameAndParentValue(String dictName, String parentValue) {
        if (StringsUtil.isBlank(dictName) || StringsUtil.isBlank(parentValue)) {
            return Future.succeededFuture(null);
        }
        String key = String.format(RedisPrefix.DICT_PARENT_VALUE_PREFIX, dictName, parentValue);
        return dictParentValueCache.getIfPresent(key).compose(lv -> {
                    if (lv != null) {
                        return Future.succeededFuture(lv);
                    } else {
                        return redis.get(key).compose(rv -> {
                            if (rv != null) {
                                List<DictItemCacheVo> dictItemCacheVos = JsonUtil.readList(rv.toString(), DictItemCacheVo.class);
                                dictParentValueCache.put(key, dictItemCacheVos);
                                return Future.succeededFuture(dictItemCacheVos);
                            } else {
                                DictItemCacheVo dictItemCacheVo = new DictItemCacheVo(dictName);
                                dictItemCacheVo.setParentValue(parentValue);
                                return findDictItemCacheVoList(dictItemCacheVo).compose(dictItemCacheVos -> {
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
    public Future<DictItemCacheVo> findDictCacheByDictNameAndParentValueAndValue(String dictName, String parentValue, String value) {
        if (StringsUtil.isBlank(dictName) || StringsUtil.isBlank(parentValue) || StringsUtil.isBlank(value)) {
            return Future.succeededFuture(null);
        }
        String key = String.format(RedisPrefix.DICT_PARENT_VALUE_AND_VALUE_PREFIX, dictName, parentValue, value);
        return dictParentValueAndValueCache.getIfPresent(key).compose(lv -> {
                    if (lv != null) {
                        return Future.succeededFuture(lv);
                    } else {
                        return redis.get(key).compose(rv -> {
                            if (rv != null) {
                                DictItemCacheVo dictItemCacheVo = JsonUtil.readValue(rv.toString(), DictItemCacheVo.class);
                                dictParentValueAndValueCache.put(key, dictItemCacheVo);
                                return Future.succeededFuture(dictItemCacheVo);
                            } else {
                                return findDictItemCacheVoList(new DictItemCacheVo(dictName, parentValue, value))
                                        .flatMap(dictItemCacheVos -> {
                                                    if (dictItemCacheVos.isEmpty()) {
                                                        return Future.succeededFuture(new DictItemCacheVo());
                                                    } else if (dictItemCacheVos.size() == 1) {
                                                        return Future.succeededFuture(dictItemCacheVos.get(0));
                                                    } else {
                                                        return Future.failedFuture("find dict cache by dictName and value more than one");
                                                    }
                                                }
                                        ).compose(dictItemCacheVo -> {
                                            String expireTime = String.valueOf(redisExpire.getSeconds() + new Random().nextLong(100) + 1L);
                                            redis.set(List.of(key, JsonUtil.toJson(dictItemCacheVo), "EX", expireTime));
                                            return Future.succeededFuture(dictItemCacheVo);
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
