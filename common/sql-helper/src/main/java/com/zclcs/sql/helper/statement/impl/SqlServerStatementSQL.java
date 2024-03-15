package com.zclcs.sql.helper.statement.impl;

import com.zclcs.sql.helper.statement.bean.SqlAndParams;
import com.zclcs.sql.helper.statement.bean.SqlAssist;
import com.zclcs.sql.helper.statement.bean.SqlPropertyValue;
import com.zclcs.sql.helper.statement.bean.SqlWhereCondition;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.sqlclient.Tuple;

import java.util.List;

/**
 * SQL Server通用SQL操作
 *
 * @author <a href="https://github.com/zclcs">zclcs</a>
 */
public class SqlServerStatementSQL extends AbstractStatementSQL {
    public SqlServerStatementSQL(Class<?> entity) {
        super(entity);
    }

    /**
     * 日志工具
     */
    private final Logger LOG = LoggerFactory.getLogger(AbstractStatementSQL.class);
    /**
     * 分页的排序,子类可以重写该方法
     *
     * @return
     */

    /**
     * 分页时指定排序的SQL语句,默认排序主键,子类可以重写该方法排序其他的列<br>
     * tips:当assist.getOrder()不为空时则该方法无效
     *
     * @param cols
     * @return
     */
    protected String getRowNumberOverSQL(String cols) {
        return " order by " + cols + " ";
    }

    @Override
    public SqlAndParams selectAllSQL(SqlAssist assist) {
        if (assist != null && assist.getRowSize() != null) {
            // 去重语句
            String distinct = assist.getDistinct() == null ? "" : assist.getDistinct();
            // 表的列名
            String column = assist.getResultColumn() == null ? resultColumns() : assist.getResultColumn();
            StringBuilder sql = new StringBuilder();
            // SQL语句添加分页
            sql.append("select * from ( ");
            sql.append(String.format("select %s %s,row_number() over(", distinct, column));
            if (assist.getOrder() != null) {
                sql.append(assist.getOrder());
            } else {
                sql.append(getRowNumberOverSQL(primaryId()));
            }
            sql.append(String.format(") AS tt_row_index from %s ", tableName()));
            // 参数
            Tuple params = Tuple.tuple();
            if (assist.getJoinOrReference() != null) {
                sql.append(assist.getJoinOrReference());
            }
            if (assist.getCondition() != null && !assist.getCondition().isEmpty()) {
                List<SqlWhereCondition<?>> where = assist.getCondition();
                sql.append(" where ").append(where.get(0).getRequire());
                if (where.get(0).getValue() != null) {
                    params.addValue(where.get(0).getValue());
                }
                if (where.get(0).getValues() != null) {
                    for (Object value : where.get(0).getValues()) {
                        params.addValue(value);
                    }
                }
                for (int i = 1; i < where.size(); i++) {
                    sql.append(where.get(i).getRequire());
                    if (where.get(i).getValue() != null) {
                        params.addValue(where.get(i).getValue());
                    }
                    if (where.get(i).getValues() != null) {
                        for (Object value : where.get(i).getValues()) {
                            params.addValue(value);
                        }
                    }
                }
            }
            if (assist.getGroupBy() != null) {
                sql.append(" group by ").append(assist.getGroupBy()).append(" ");
            }
            if (assist.getHaving() != null) {
                sql.append(" having ").append(assist.getHaving()).append(" ");
                if (assist.getHavingValue() != null) {
                    for (Object value : assist.getHavingValue()) {
                        params.addValue(value);
                    }
                }
            }
            // SQL分页语句添加别名与结尾
            sql.append(" ) AS tt_result_table ");
            sql.append(" where tt_row_index > ? and tt_row_index <= ? ");
            long startRow = assist.getStartRow() == null ? 0 : assist.getStartRow();
            params.addValue(startRow);
            params.addValue(startRow + assist.getRowSize());
            SqlAndParams result = new SqlAndParams(sql.toString(), (params.size() <= 0 ? null : params));
            if (LOG.isDebugEnabled()) {
                LOG.debug("SelectAllSQL : " + result);
            }
            return result;
        } else {
            return super.selectAllSQL(assist);
        }
    }

    @Override
    public <T> SqlAndParams selectByObjSQL(T obj, String resultColumns, String tableAlias, String joinOrReference, boolean single) {
        StringBuilder sql = new StringBuilder(
                String.format("select %s %s from %s %s ", (single ? "top 1" : ""), (resultColumns == null ? resultColumns() : resultColumns),
                        (tableName() + (tableAlias == null ? "" : (" AS " + tableAlias))),
                        (joinOrReference == null ? "" : joinOrReference)));
        Tuple params = Tuple.tuple();
        boolean isFrist = true;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (int i = propertyValue.size() - 1; i >= 0; i--) {
            SqlPropertyValue<?> pv = propertyValue.get(i);
            if (pv.getValue() != null) {
                if (isFrist) {
                    sql.append(String.format("where %s = ? ", (tableAlias == null ? "" : (tableAlias + ".")) + pv.getName()));
                    isFrist = false;
                } else {
                    sql.append(String.format("and %s = ? ", (tableAlias == null ? "" : (tableAlias + ".")) + pv.getName()));
                }
                params.addValue(pv.getValue());
            }
        }
        SqlAndParams result = new SqlAndParams(sql.toString(), (params.size() <= 0 ? null : params));
        if (LOG.isDebugEnabled()) {
            LOG.debug("selectByObjSQL : " + result);
        }
        return result;
    }

}
