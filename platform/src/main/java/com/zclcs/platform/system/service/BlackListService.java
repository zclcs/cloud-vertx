package com.zclcs.platform.system.service;

import com.zclcs.platform.system.dao.entity.BlackList;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface BlackListService {

    /**
     * 获取黑名单
     *
     * @return 黑名单
     */
    Future<List<BlackList>> getEnableBlackList();

    /**
     * 获取黑名单
     *
     * @return 黑名单
     */
    Future<List<BlackList>> getEnableBlackListCache();

}
