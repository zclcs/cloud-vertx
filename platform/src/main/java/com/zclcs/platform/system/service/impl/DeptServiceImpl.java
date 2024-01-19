package com.zclcs.platform.system.service.impl;

import com.zclcs.cloud.core.bean.Tree;
import com.zclcs.cloud.core.constant.CommonCore;
import com.zclcs.cloud.core.utils.TreeUtil;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.platform.system.dao.vo.DeptTreeVo;
import com.zclcs.platform.system.dao.vo.DeptVo;
import com.zclcs.platform.system.dao.vo.DeptVoRowMapper;
import com.zclcs.platform.system.service.DeptService;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zclcs
 */
public class DeptServiceImpl implements DeptService {

    private final SqlClient sqlClient;

    public DeptServiceImpl(SqlClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public Future<List<DeptVo>> getDeptVoList(DeptVo deptVo) {
        return SqlTemplate.forQuery(sqlClient, """
                        SELECT 
                        `dept_id`, 
                        `dept_code`, 
                        `parent_code`, 
                        `dept_name`, 
                        `order_num`, 
                        `create_at`, 
                        `update_at` 
                        FROM `system_dept` 
                        ORDER BY `order_num` ASC
                        """)
                .mapTo(DeptVoRowMapper.INSTANCE)
                .execute(deptVo.toMap())
                .flatMap(rows -> {
                    if (rows.size() > 0) {
                        List<DeptVo> deptVoList = new ArrayList<>();
                        rows.forEach(deptVoList::add);
                        return Future.succeededFuture(deptVoList);
                    }
                    return Future.succeededFuture(null);
                })
                ;
    }

    @Override
    public Future<List<Tree<DeptTreeVo>>> getDeptTree(DeptVo deptVo) {
        String condition = getDeptVoCondition(deptVo);
        return SqlTemplate.forQuery(sqlClient, getDeptVoSql(condition))
                .mapTo(DeptVoRowMapper.INSTANCE)
                .execute(deptVo.toMap())
                .flatMap(rows -> {
                    List<Tree<DeptTreeVo>> deptTreeVoList = new ArrayList<>();
                    rows.forEach(row -> {
                        Tree<DeptTreeVo> deptTreeVo = new Tree<>();
                        deptTreeVo.setId(row.getDeptId());
                        deptTreeVo.setCode(row.getDeptCode());
                        deptTreeVo.setParentCode(row.getParentCode());
                        deptTreeVo.setLabel(row.getDeptName());
                        DeptTreeVo extra = new DeptTreeVo();
                        extra.setCreateAt(row.getCreateAt());
                        extra.setOrderNum(row.getOrderNum());
                        extra.setHarPar(!row.getParentCode().equals(CommonCore.TOP_PARENT_CODE));
                        extra.setDeptName(row.getDeptName());
                        deptTreeVo.setExtra(extra);
                        deptTreeVoList.add(deptTreeVo);
                    });
                    if (StringsUtil.isNotBlank(condition)) {
                        return Future.succeededFuture(deptTreeVoList);
                    }
                    return Future.succeededFuture(TreeUtil.build(deptTreeVoList));
                })
                ;
    }

    private String getDeptVoSql(String condition) {
        String sql = """
                SELECT 
                `dept_id`, 
                `dept_code`, 
                `parent_code`, 
                `dept_name`, 
                `order_num`, 
                `create_at`, 
                `update_at` 
                FROM `system_dept` 
                where 1=1
                %s
                ORDER BY `order_num` ASC
                """;
        return String.format(sql, condition);
    }

    private String getDeptVoCondition(DeptVo deptVo) {
        String condition = "";
        if (StringsUtil.isNotBlank(deptVo.getDeptName())) {
            condition += " and `dept_name` like concat('%', #{deptName}, '%')";
        }
        return condition;
    }

}
