package com.zclcs.platform.system.service;

import com.zclcs.cloud.core.bean.Tree;
import com.zclcs.platform.system.dao.vo.DeptTreeVo;
import com.zclcs.platform.system.dao.vo.DeptVo;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author zclcs
 */
public interface DeptService {

    /**
     * 获取部门列表
     *
     * @param deptVo 部门信息
     * @return 部门列表
     */
    Future<List<DeptVo>> getDeptVoList(DeptVo deptVo);

    /**
     * 获取部门树
     *
     * @param deptVo 部门信息
     * @return 部门树
     */
    Future<List<Tree<DeptTreeVo>>> getDeptTree(DeptVo deptVo);
}
