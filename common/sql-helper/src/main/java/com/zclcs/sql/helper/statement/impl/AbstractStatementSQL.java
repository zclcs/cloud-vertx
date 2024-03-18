package com.zclcs.sql.helper.statement.impl;

import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.sql.helper.annotation.Table;
import com.zclcs.sql.helper.annotation.TableColumn;
import com.zclcs.sql.helper.annotation.TableId;
import com.zclcs.sql.helper.statement.SQLStatement;
import com.zclcs.sql.helper.statement.bean.SqlAndParams;
import com.zclcs.sql.helper.statement.bean.SqlAssist;
import com.zclcs.sql.helper.statement.bean.SqlPropertyValue;
import com.zclcs.sql.helper.statement.bean.SqlWhereCondition;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.sqlclient.Tuple;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象数据库操作语句,默认以MySQL标准来编写,如果其他数据库可以基础并重写不兼容的方法<br>
 * 通常不支持limit分页的数据库需要重写{@link #selectAllSQL(SqlAssist)}与{@link #selectByObjSQL(Object, String, String, String, boolean)}这两个方法
 *
 * @author <a href="https://github.com/zclcs">zclcs</a>
 */
public abstract class AbstractStatementSQL implements SQLStatement {
    /**
     * 日志工具
     */
    private final Logger LOG = LoggerFactory.getLogger(AbstractStatementSQL.class);
    /**
     * 表的名称
     */
    private String sqlTableName;
    /**
     * 主键的名称
     */
    private String sqlPrimaryId;
    /**
     * 主键对应实体类属性名
     */
    private String entityFieldNamePrimaryId;
    /**
     * 返回列
     */
    private String sqlResultColumns;

    public AbstractStatementSQL(Class<?> entity) {
        super();
        if (tableName() == null) {
            Table table = entity.getAnnotation(Table.class);
            if (table == null || table.name().isEmpty()) {
                throw new NullPointerException(entity.getName() + " no Table annotation ,you need to set @Table on the class");
            }
            this.sqlTableName = table.name();
        }
        if (primaryId() == null || resultColumns() == null) {
            boolean hasId = false;
            Field[] fields = entity.getDeclaredFields();
            StringBuilder column = new StringBuilder();
            for (Field field : fields) {
                field.setAccessible(true);
                TableId tableId = field.getAnnotation(TableId.class);
                TableColumn tableCol = field.getAnnotation(TableColumn.class);
                if (tableId == null && tableCol == null) {
                    continue;
                }
                if (tableId != null) {
                    if (tableId.name() == null || tableId.name().isEmpty()) {
                        continue;
                    }
                    this.sqlPrimaryId = tableId.name();
                    this.entityFieldNamePrimaryId = field.getName();
                    hasId = true;
                    column.append(",").append(tableId.name());
                    if (tableId.alias() != null && !tableId.alias().isEmpty()) {
                        column.append(" AS \"").append(tableId.alias()).append("\"");
                    }
                } else {
                    column.append(",").append(tableCol.name());
                    if (tableCol.alias() != null && !tableCol.alias().isEmpty()) {
                        column.append(" AS \"").append(tableCol.alias()).append("\"");
                    }
                }
            }
            if (!hasId) {
                throw new NullPointerException(entity.getName() + " no TableId annotation ,you need to set @TableId on the field");
            }
            this.sqlResultColumns = column.substring(1);
        }

    }

    /**
     * 获取表名称
     *
     * @return 表名称
     */
    @Override
    public String tableName() {
        return sqlTableName;
    }

    /**
     * 获取主键名称
     *
     * @return 主键名称
     */
    @Override
    public String primaryId() {
        return sqlPrimaryId;
    }

    /**
     * 获取表返回列
     *
     * @return 表返回列
     */
    @Override
    public String resultColumns() {
        return sqlResultColumns;
    }

    /**
     * 表的所有列名与列名对应的值
     *
     * @return {@link SqlPropertyValue}
     */
    @Override
    public <T> List<SqlPropertyValue<?>> getPropertyValue(T obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<SqlPropertyValue<?>> result = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            TableId tableId = field.getAnnotation(TableId.class);
            TableColumn tableCol = field.getAnnotation(TableColumn.class);
            if (tableId == null && tableCol == null) {
                continue;
            }
            if (tableId != null) {
                result.add(0, new SqlPropertyValue<>(tableId.name(), field.get(obj)));
            } else {
                result.add(new SqlPropertyValue<>(tableCol.name(), field.get(obj)));
            }
        }
        return result;
    }

    @Override
    public Object primaryKeyValue(List<SqlPropertyValue<?>> propertyValue) {
        if (sqlPrimaryId == null || sqlPrimaryId.isEmpty()) {
            return null;
        }
        for (SqlPropertyValue<?> sqlPropertyValue : propertyValue) {
            if (sqlPropertyValue.getName().equalsIgnoreCase(sqlPrimaryId)) {
                Object value = sqlPropertyValue.getValue();
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public Boolean isPrimaryHasValue(Object primaryKeyValue) {
        if (primaryKeyValue != null) {
            if (primaryKeyValue instanceof String s) {
                if (StringsUtil.isBlank(s)) {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public <T> void setPrimaryId(T entity, Object primaryKeyValue) throws Exception {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(entityFieldNamePrimaryId)) {
                field.set(entity, primaryKeyValue);
                return;
            }
        }
    }

    @Override
    public SqlAndParams getCountSQL(SqlAssist assist) {
        StringBuilder sql = new StringBuilder(String.format("select count(*) from %s ", tableName()));
        Tuple params = null;
        if (assist != null) {
            if (assist.getJoinOrReference() != null) {
                sql.append(assist.getJoinOrReference());
            }
            if (assist.getCondition() != null && !assist.getCondition().isEmpty()) {
                params = Tuple.tuple();
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
        }
        SqlAndParams result = new SqlAndParams(sql.toString(), params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getCountSQL : " + result);
        }
        return result;
    }

    @Override
    public SqlAndParams countByCustomSql(String sqlString, SqlAssist assist) {
        StringBuilder sql = new StringBuilder(sqlString);
        Tuple params = null;
        if (assist != null) {
            if (assist.getCondition() != null && !assist.getCondition().isEmpty()) {
                params = Tuple.tuple();
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
        }
        SqlAndParams result = new SqlAndParams(String.format("select count(*) from ( %s ) tmp ", sql), params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("countByCustomSql : " + result);
            if (assist != null) {
                LOG.debug("countByCustomSql : " + assist.getCondition());
            }
        }
        return result;
    }

    @Override
    public SqlAndParams selectAllSQL(SqlAssist assist) {
        // 如果Assist为空返回默认默认查询语句,反则根据Assist生成语句sql语句
        if (assist == null) {
            SqlAndParams result = new SqlAndParams(String.format("select %s from %s ", resultColumns(), tableName()));
            if (LOG.isDebugEnabled()) {
                LOG.debug("SelectAllSQL : " + result.toString());
            }
            return result;
        } else {
            // 去重语句
            String distinct = assist.getDistinct() == null ? "" : assist.getDistinct();
            // 表的列名
            String column = assist.getResultColumn() == null ? resultColumns() : assist.getResultColumn();
            // 初始化SQL语句
            StringBuilder sql = new StringBuilder(String.format("select %s %s from %s", distinct, column, tableName()));
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
            if (assist.getOrder() != null) {
                sql.append(assist.getOrder());
            }
            if (assist.getRowSize() != null || assist.getStartRow() != null) {
                if (assist.getStartRow() != null) {
                    sql.append(" LIMIT ?");
                    params.addValue(assist.getRowSize());
                }
                if (assist.getStartRow() != null) {
                    sql.append(" OFFSET ?");
                    params.addValue(assist.getStartRow());
                }
            }
            SqlAndParams result = new SqlAndParams(sql.toString(), (params.size() <= 0 ? null : params));
            if (LOG.isDebugEnabled()) {
                LOG.debug("SelectAllSQL : " + result);
            }
            return result;
        }
    }

    @Override
    public <S> SqlAndParams selectByIdSQL(S primaryValue, String resultColumns, String tableAlias, String joinOrReference) {
        String sql = String.format("select %s from %s %s where %s = ? ",
                (resultColumns == null ? resultColumns() : resultColumns),
                (tableName() + (tableAlias == null ? "" : (" AS " + tableAlias))),
                (joinOrReference == null ? "" : joinOrReference),
                (tableAlias == null ? "" : (tableAlias + ".")) + primaryId());
        SqlAndParams result = new SqlAndParams(sql, Tuple.of(primaryValue));
        if (LOG.isDebugEnabled()) {
            LOG.debug("selectByIdSQL : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams selectByObjSQL(T obj, String resultColumns, String tableAlias, String joinOrReference, boolean single) {
        StringBuilder sql = new StringBuilder(String.format("select %s from %s %s ",
                (resultColumns == null ? resultColumns() : resultColumns),
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
        if (single) {
            sql.append(" LIMIT 1");
        }
        SqlAndParams result = new SqlAndParams(sql.toString(), (params.size() <= 0 ? null : params));
        if (LOG.isDebugEnabled()) {
            LOG.debug("selectByObjSQL : " + result);
        }
        return result;
    }

    @Override
    public SqlAndParams selectByCustomSql(String sqlString, SqlAssist sqlAssist) {
        StringBuilder sql = new StringBuilder(sqlString);
        Tuple params = Tuple.tuple();
        if (sqlAssist.getCondition() != null && !sqlAssist.getCondition().isEmpty()) {
            List<SqlWhereCondition<?>> where = sqlAssist.getCondition();
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
        if (sqlAssist.getGroupBy() != null) {
            sql.append(" group by ").append(sqlAssist.getGroupBy()).append(" ");
        }
        if (sqlAssist.getHaving() != null) {
            sql.append(" having ").append(sqlAssist.getHaving()).append(" ");
            if (sqlAssist.getHavingValue() != null) {
                for (Object value : sqlAssist.getHavingValue()) {
                    params.addValue(value);
                }
            }
        }
        if (sqlAssist.getOrder() != null) {
            sql.append(sqlAssist.getOrder());
        }
        if (sqlAssist.getRowSize() != null || sqlAssist.getStartRow() != null) {
            if (sqlAssist.getStartRow() != null) {
                sql.append(" LIMIT ?");
                params.addValue(sqlAssist.getRowSize());
            }
            if (sqlAssist.getStartRow() != null) {
                sql.append(" OFFSET ?");
                params.addValue(sqlAssist.getStartRow());
            }
        }
        SqlAndParams result = new SqlAndParams(sql.toString(), (params.size() <= 0 ? null : params));
        if (LOG.isDebugEnabled()) {
            LOG.debug("selectByCustomSql : " + result);
        }
        return result;
    }

    @Override
    public SqlAndParams selectByCustomSql(String sqlString, Tuple params) {
        SqlAndParams result = new SqlAndParams(sqlString, (params.size() <= 0 ? null : params));
        if (LOG.isDebugEnabled()) {
            LOG.debug("selectByCustomSql : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams insertAllSQL(T obj) {
        Tuple params = Tuple.tuple();
        StringBuilder tempColumn = null;
        StringBuilder tempValues = null;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (tempColumn == null) {
                tempColumn = new StringBuilder(pv.getName());
                tempValues = new StringBuilder("?");
            } else {
                tempColumn.append(",").append(pv.getName());
                tempValues.append(",?");
            }
            params.addValue(pv.getValue());
        }
        String sql = String.format("insert into %s (%s) values (%s) ", tableName(), tempColumn, tempValues);
        SqlAndParams result = new SqlAndParams(sql, (params.size() <= 0 ? null : params));
        if (LOG.isDebugEnabled()) {
            LOG.debug("insertAllSQL : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams insertNonEmptySQL(T obj) {
        Tuple params = Tuple.tuple();
        StringBuilder tempColumn = null;
        StringBuilder tempValues = null;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (pv.getValue() != null) {
                if (tempColumn == null) {
                    tempColumn = new StringBuilder(pv.getName());
                    tempValues = new StringBuilder("?");
                } else {
                    tempColumn.append(",").append(pv.getName());
                    tempValues.append(",?");
                }
                params.addValue(pv.getValue());
            }
        }
        if (tempColumn == null) {
            return new SqlAndParams(false, "The column or value is null");
        }
        String sql = String.format("insert into %s (%s) values (%s) ", tableName(), tempColumn, tempValues);
        SqlAndParams result = new SqlAndParams(sql, (params.size() <= 0 ? null : params));
        if (LOG.isDebugEnabled()) {
            LOG.debug("insertNonEmptySQL : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams insertBatchSQL(List<T> list) {
        if (list == null || list.isEmpty()) {
            return new SqlAndParams(false, "The param can not be null or empty");
        }
        StringBuilder tempColumn = null;
        StringBuilder tempValues = null;
        Tuple param0 = Tuple.tuple();
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(list.get(0));
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (tempColumn == null) {
                tempColumn = new StringBuilder(pv.getName());
                tempValues = new StringBuilder("?");
            } else {
                tempColumn.append(",").append(pv.getName());
                tempValues.append(",?");
            }
            param0.addValue(pv.getValue());
        }
        List<Tuple> params = new ArrayList<>();
        params.add(param0);
        for (int i = 1; i < list.size(); i++) {
            Tuple paramx = Tuple.tuple();
            List<SqlPropertyValue<?>> propertyValue1;
            try {
                propertyValue1 = getPropertyValue(list.get(i));
            } catch (Exception e) {
                return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
            }
            for (SqlPropertyValue<?> pv : propertyValue1) {
                paramx.addValue(pv.getValue());
            }
            params.add(paramx);
        }
        String sql = String.format("insert into %s (%s) values (%s) ", tableName(), tempColumn, tempValues);
        SqlAndParams qp = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("insertBatch : " + qp);
        }
        return qp;
    }

    @Override
    public SqlAndParams insertBatchSQL(List<String> columns, List<Tuple> params) {
        if (columns == null || columns.isEmpty()) {
            return new SqlAndParams(false, "The columns can not be null or empty");
        }
        if (params == null || params.isEmpty()) {
            return new SqlAndParams(false, "The params can not be null or empty");
        }
        StringBuilder tempColumn = null;
        StringBuilder tempValues = null;
        for (String column : columns) {
            if (tempColumn == null) {
                tempColumn = new StringBuilder(column);
                tempValues = new StringBuilder("?");
            } else {
                tempColumn.append(",").append(column);
                tempValues.append(",?");
            }
        }
        String sql = String.format("insert into %s (%s) values (%s) ", tableName(), tempColumn, tempValues);
        SqlAndParams qp = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("insertBatch : " + qp);
        }
        return qp;
    }

    @Override
    public <T> SqlAndParams replaceSQL(T obj) {
        Tuple params = Tuple.tuple();
        StringBuilder tempColumn = null;
        StringBuilder tempValues = null;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (pv.getValue() != null) {
                if (tempColumn == null) {
                    tempColumn = new StringBuilder(pv.getName());
                    tempValues = new StringBuilder("?");
                } else {
                    tempColumn.append(",").append(pv.getName());
                    tempValues.append(",?");
                }
                params.addValue(pv.getValue());
            }
        }
        if (tempColumn == null) {
            return new SqlAndParams(false, "The column or value is null");
        }
        String sql = String.format("replace into %s (%s) values (%s) ", tableName(), tempColumn, tempValues);
        SqlAndParams result = new SqlAndParams(sql.toString(), (params.size() <= 0 ? null : params));
        if (LOG.isDebugEnabled()) {
            LOG.debug("replaceSQL : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams updateAllByIdSQL(T obj) {
        if (primaryId() == null) {
            return new SqlAndParams(false, "there is no primary key in your SQL statement");
        }
        Tuple params = Tuple.tuple();
        StringBuilder tempColumn = null;
        Object tempIdValue = null;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (pv.getName().equals(primaryId())) {
                tempIdValue = pv.getValue();
                continue;
            }
            if (tempColumn == null) {
                tempColumn = new StringBuilder(pv.getName() + " = ? ");
            } else {
                tempColumn.append(", ").append(pv.getName()).append(" = ? ");
            }
            params.addValue(pv.getValue());
        }
        if (tempIdValue == null) {
            return new SqlAndParams(false, "there is no primary key in your SQL statement");
        }
        params.addValue(tempIdValue);
        String sql = String.format("update %s set %s where %s = ? ", tableName(), tempColumn, primaryId());
        SqlAndParams result = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateAllByIdSQL : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams updateAllByAssistSQL(T obj, SqlAssist assist) {
        if (assist == null || assist.getCondition() == null || assist.getCondition().isEmpty()) {
            return new SqlAndParams(false, "SqlAssist or SqlAssist.condition is null");
        }
        Tuple params = Tuple.tuple();
        StringBuilder tempColumn = null;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (tempColumn == null) {
                tempColumn = new StringBuilder(pv.getName() + " = ? ");
            } else {
                tempColumn.append(", ").append(pv.getName()).append(" = ? ");
            }
            params.addValue(pv.getValue());
        }
        List<SqlWhereCondition<?>> where = assist.getCondition();
        StringBuilder whereStr = new StringBuilder(" where " + where.get(0).getRequire());
        if (where.get(0).getValue() != null) {
            params.addValue(where.get(0).getValue());
        }
        if (where.get(0).getValues() != null) {
            for (Object value : where.get(0).getValues()) {
                params.addValue(value);
            }
        }
        for (int i = 1; i < where.size(); i++) {
            whereStr.append(where.get(i).getRequire());
            if (where.get(i).getValue() != null) {
                params.addValue(where.get(i).getValue());
            }
            if (where.get(i).getValues() != null) {
                for (Object value : where.get(i).getValues()) {
                    params.addValue(value);
                }
            }
        }
        String sql = String.format("update %s set %s %s", tableName(), tempColumn, whereStr);
        SqlAndParams result = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateAllByAssistSQL : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams updateNonEmptyByIdSQL(T obj) {
        if (primaryId() == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("there is no primary key in your SQL statement");
            }
            return new SqlAndParams(false, "there is no primary key in your SQL statement");
        }
        Tuple params = Tuple.tuple();
        StringBuilder tempColumn = null;
        Object tempIdValue = null;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (pv.getName().equals(primaryId())) {
                tempIdValue = pv.getValue();
                continue;
            }
            if (pv.getValue() != null) {
                if (tempColumn == null) {
                    tempColumn = new StringBuilder(pv.getName() + " = ? ");
                } else {
                    tempColumn.append(", ").append(pv.getName()).append(" = ? ");
                }
                params.addValue(pv.getValue());
            }
        }
        if (tempColumn == null || tempIdValue == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("there is no set update value or no primary key in your SQL statement");
            }
            return new SqlAndParams(false, "there is no set update value or no primary key in your SQL statement");
        }
        params.addValue(tempIdValue);
        String sql = String.format("update %s set %s where %s = ? ", tableName(), tempColumn, primaryId());
        SqlAndParams result = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateNonEmptyByIdSQL : " + result);
        }
        return result;
    }

    @Override
    public <T> SqlAndParams updateNonEmptyByAssistSQL(T obj, SqlAssist assist) {
        if (assist == null || assist.getCondition() == null || assist.getCondition().size() < 1) {
            return new SqlAndParams(false, "SqlAssist or SqlAssist.condition is null");
        }
        Tuple params = Tuple.tuple();
        StringBuilder tempColumn = null;
        List<SqlPropertyValue<?>> propertyValue;
        try {
            propertyValue = getPropertyValue(obj);
        } catch (Exception e) {
            return new SqlAndParams(false, " Get SqlPropertyValue failed: " + e.getMessage());
        }
        for (SqlPropertyValue<?> pv : propertyValue) {
            if (pv.getValue() != null) {
                if (tempColumn == null) {
                    tempColumn = new StringBuilder(pv.getName() + " = ? ");
                } else {
                    tempColumn.append(", " + pv.getName() + " = ? ");
                }
                params.addValue(pv.getValue());
            }
        }

        if (tempColumn == null) {
            return new SqlAndParams(false, "The object has no value");
        }

        List<SqlWhereCondition<?>> where = assist.getCondition();
        StringBuilder whereStr = new StringBuilder(" where " + where.get(0).getRequire());
        if (where.get(0).getValue() != null) {
            params.addValue(where.get(0).getValue());
        }
        if (where.get(0).getValues() != null) {
            for (Object value : where.get(0).getValues()) {
                params.addValue(value);
            }
        }
        for (int i = 1; i < where.size(); i++) {
            whereStr.append(where.get(i).getRequire());
            if (where.get(i).getValue() != null) {
                params.addValue(where.get(i).getValue());
            }
            if (where.get(i).getValues() != null) {
                for (Object value : where.get(i).getValues()) {
                    params.addValue(value);
                }
            }
        }
        String sql = String.format("update %s set %s %s", tableName(), tempColumn, whereStr);
        SqlAndParams result = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateNonEmptyByAssistSQL : " + result);
        }
        return result;
    }

    @Override
    public <S> SqlAndParams updateSetNullByIdSQL(S primaryValue, List<String> columns) {
        if (primaryId() == null) {
            return new SqlAndParams(false, "there is no primary key in your SQL statement");
        }

        if (columns == null || columns.isEmpty()) {
            return new SqlAndParams(false, "Columns cannot be null or empty");
        }
        StringBuilder setStr = new StringBuilder();
        setStr.append(" ").append(columns.get(0)).append(" = null ");
        for (int i = 1; i < columns.size(); i++) {
            setStr.append(", ").append(columns.get(i)).append(" = null ");
        }
        String sql = String.format("update %s set %s where %s = ? ", tableName(), setStr, primaryId());
        SqlAndParams result = new SqlAndParams(sql, Tuple.of(primaryValue));
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateSetNullById : " + result);
        }
        return result;
    }

    @Override
    public <S> SqlAndParams updateSetNullByAssistSQL(SqlAssist assist, List<String> columns) {
        if (assist == null || assist.getCondition() == null || assist.getCondition().isEmpty()) {
            return new SqlAndParams(false, "SqlAssist or SqlAssist.condition is null");
        }
        if (columns == null || columns.isEmpty()) {
            return new SqlAndParams(false, "Columns cannot be null or empty");
        }
        StringBuilder setStr = new StringBuilder();
        setStr.append(" ").append(columns.get(0)).append(" = null ");
        for (int i = 1; i < columns.size(); i++) {
            setStr.append(", ").append(columns.get(i)).append(" = null ");
        }
        Tuple params = Tuple.tuple();
        List<SqlWhereCondition<?>> where = assist.getCondition();
        StringBuilder whereStr = new StringBuilder(" where " + where.get(0).getRequire());
        if (where.get(0).getValue() != null) {
            params.addValue(where.get(0).getValue());
        }
        if (where.get(0).getValues() != null) {
            for (Object value : where.get(0).getValues()) {
                params.addValue(value);
            }
        }
        for (int i = 1; i < where.size(); i++) {
            whereStr.append(where.get(i).getRequire());
            if (where.get(i).getValue() != null) {
                params.addValue(where.get(i).getValue());
            }
            if (where.get(i).getValues() != null) {
                for (Object value : where.get(i).getValues()) {
                    params.addValue(value);
                }
            }
        }
        String sql = String.format("update %s set %s %s", tableName(), setStr, whereStr);
        SqlAndParams result = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateSetNullByAssist : " + result);
        }
        return result;
    }

    @Override
    public <S> SqlAndParams deleteByIdSQL(S primaryValue) {
        if (primaryId() == null) {
            return new SqlAndParams(false, "there is no primary key in your SQL statement");
        }
        String sql = String.format("delete from %s where %s = ? ", tableName(), primaryId());
        SqlAndParams result = new SqlAndParams(sql, Tuple.of(primaryValue));
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleteByIdSQL : " + result);
        }
        return result;
    }

    @Override
    public <S> SqlAndParams deleteByIdsSQL(List<S> primaryValues) {
        if (primaryId() == null) {
            return new SqlAndParams(false, "there is no primary key in your SQL statement");
        }
        String sql = String.format("delete from %s where %s in ( ", tableName(), primaryId());
        StringBuilder sb = new StringBuilder(sql);
        sb.append("?,".repeat(primaryValues.size()));
        sql = sb.deleteCharAt(sb.length() - 1).append(" )").toString();
        Tuple params = Tuple.tuple();
        for (S primaryValue : primaryValues) {
            params.addValue(primaryValue);
        }
        SqlAndParams result = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleteByIdsSQL : " + result);
        }
        return result;
    }

    @Override
    public SqlAndParams deleteByAssistSQL(SqlAssist assist) {
        if (assist == null || assist.getCondition() == null || assist.getCondition().isEmpty()) {
            return new SqlAndParams(false, "SqlAssist or SqlAssist.condition is null");
        }
        List<SqlWhereCondition<?>> where = assist.getCondition();
        Tuple params = Tuple.tuple();
        StringBuilder whereStr = new StringBuilder(" where " + where.get(0).getRequire());
        if (where.get(0).getValue() != null) {
            params.addValue(where.get(0).getValue());
        }
        if (where.get(0).getValues() != null) {
            for (Object value : where.get(0).getValues()) {
                params.addValue(value);
            }
        }
        ;
        for (int i = 1; i < where.size(); i++) {
            whereStr.append(where.get(i).getRequire());
            if (where.get(i).getValue() != null) {
                params.addValue(where.get(i).getValue());
            }
            if (where.get(i).getValues() != null) {
                for (Object value : where.get(i).getValues()) {
                    params.addValue(value);
                }
            }
        }
        String sql = String.format("delete from %s %s", tableName(), whereStr);
        SqlAndParams result = new SqlAndParams(sql, params);
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleteByAssistSQL : " + result);
        }
        return result;
    }

}
