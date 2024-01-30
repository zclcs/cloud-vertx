package com.zclcs.platform.system.service;

import com.zclcs.cloud.core.bean.Tree;
import com.zclcs.common.core.constant.StringPool;
import com.zclcs.platform.system.dao.cache.DictItemCacheVo;
import com.zclcs.platform.system.dao.vo.DictItemTreeVo;
import io.vertx.core.Future;

import java.util.List;

/**
 * 字典 Service接口
 *
 * @author zclcs
 * @since 2023-09-01 20:03:54.686
 */
public interface DictItemService {

    /**
     * 获取字典树
     *
     * @return 字典树
     */
    Future<List<Tree<DictItemTreeVo>>> tree(String dictName);

    /**
     * 根据字典名称获取字典列表
     *
     * @param dictItemCacheVo 字典名称
     * @return 字典列表
     */
    Future<List<DictItemCacheVo>> findDictItemCacheVoList(DictItemCacheVo dictItemCacheVo);

    /**
     * 根据字典名称获取字典列表缓存
     *
     * @param dictName 字典名称
     * @return 字典列表
     */
    Future<List<DictItemCacheVo>> findDictCacheByDictName(String dictName);

    /**
     * 根据字典名称和字典值获取字典标题
     *
     * @param dictName 字典名称
     * @param value    字典值
     * @return 字典标题
     */
    default Future<String> getDictTitle(String dictName, String value) {
        return findDictCacheByDictNameAndValue(dictName, value)
                .compose(dictItemCacheVo -> dictItemCacheVo == null ? Future.succeededFuture(StringPool.EMPTY) : Future.succeededFuture(dictItemCacheVo.getTitle()));
    }

    /**
     * 根据字典名称和字典值获取字典缓存
     *
     * @param dictName 字典名称
     * @param value    字典值
     * @return 字典缓存
     */
    Future<DictItemCacheVo> findDictCacheByDictNameAndValue(String dictName, String value);

    /**
     * 根据字典名称和父级字典值获取字典缓存
     *
     * @param dictName    字典名称
     * @param parentValue 父级字典值
     * @return 字典缓存
     */
    Future<List<DictItemCacheVo>> findDictCacheByDictNameAndParentValue(String dictName, String parentValue);

    /**
     * 根据字典名称和父级字典值和字典值获取字典缓存
     *
     * @param dictName    字典名称
     * @param parentValue 父级字典值
     * @param value       字典值
     * @return 字典缓存
     */
    Future<DictItemCacheVo> findDictCacheByDictNameAndParentValueAndValue(String dictName, String parentValue, String value);

    /**
     * 验证字典是否重复
     *
     * @param dictName 字典名称
     * @param value    字典唯一值
     * @param id       字典id
     */
    Future<Void> validateDictNameAndValueAndType(String dictName, String value, Long id);

}
