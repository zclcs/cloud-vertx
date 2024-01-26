package com.zclcs.platform.system.service;

import com.zclcs.cloud.core.bean.Tree;
import com.zclcs.platform.system.dao.cache.DictItemCacheVo;
import com.zclcs.platform.system.dao.vo.DictItemTreeVo;
import com.zclcs.platform.system.dao.vo.DictItemVo;
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
     * 获取字典树
     *
     * @param dictItemVo dictItemVo
     * @return 字典树
     */
    Future<List<Tree<DictItemTreeVo>>> findDictItemTree(DictItemVo dictItemVo);

    /**
     * 根据字典名称获取字典列表
     *
     * @param dictName 字典名称
     * @return 字典列表
     */
    Future<List<DictItemCacheVo>> findByDictName(String dictName);

    Future<List<DictItemCacheVo>> findByDictNameCache(String dictName);

    /**
     * 验证字典是否重复
     *
     * @param dictName 字典名称
     * @param value    字典唯一值
     * @param id       字典id
     */
    Future<Void> validateDictNameAndValueAndType(String dictName, String value, Long id);

}
