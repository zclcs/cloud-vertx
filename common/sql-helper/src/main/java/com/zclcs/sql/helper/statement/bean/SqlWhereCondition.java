package com.zclcs.sql.helper.statement.bean;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SqlAssist的条件类,require属性为列的条件,value和values为条件值
 *
 * @param <T>
 * @author <a href="https://zclcstools.org">zclcs</a>
 */
public class SqlWhereCondition<T> {
    /**
     * 条件
     */
    private String require;
    /**
     * 条件值,单个
     */
    private T value;
    /**
     * 条件值,多个
     */
    private Object[] values;

    /**
     * 将当前对象装换为JsonObject
     *
     * @return JsonObject对象
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        if (require != null) {
            json.put("require", require);
        }
        if (value != null) {
            json.put("value", value);
        }
        if (values != null) {
            JsonArray array = new JsonArray();
            for (int i = 0; i < values.length; i++) {
                array.add(values[i]);
            }
            json.put("values", array);
        }
        return json;
    }

    /**
     * 将一个JsonObject对象装换为SqlWhereCondition
     *
     * @param obj JsonObject
     * @return {@link SqlWhereCondition}
     */
    public static SqlWhereCondition<?> fromJson(JsonObject obj) {
        SqlWhereCondition<Object> condition = new SqlWhereCondition<>();
        if (obj.getValue("require") instanceof String) {
            condition.setRequire(obj.getString("require"));
        }
        if (obj.getValue("value") != null) {
            condition.setValue(obj.getValue("value"));
        }
        if (obj.getValue("values") instanceof JsonArray) {
            List<Object> list = new ArrayList<>();
            obj.getJsonArray("values").forEach(list::add);
            condition.setValues(list.toArray());
        }
        return condition;
    }

    public SqlWhereCondition() {
        super();
    }

    public SqlWhereCondition(String require, T value) {
        super();
        this.require = require;
        this.value = value;
    }

    public SqlWhereCondition(String require, Object... values) {
        super();
        this.require = require;
        this.values = values;
    }

    /**
     * SQL: [and] column = value <br>
     * 注释: 并且列名=条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andEq(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " = ? ", value);
    }

    /**
     * SQL: [or] column = value<br>
     * 注释: 或者列名=条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orEq(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " = ? ", value);
    }

    /**
     * SQL: [and] column in (value1, value2, value3, ...) <br>
     * 注释: 并且列名在条件值列表中
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值列表
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andIn(String column, List<T> value) {
        StringBuilder sb = new StringBuilder("and " + column + " in (");
        sb.append("?,".repeat(value.size()));
        sb.deleteCharAt(sb.length() - 1).append(")");
        return new SqlWhereCondition<T>(sb.toString(), value.toArray());
    }

    /**
     * SQL: [or] column in (value1, value2, value3, ...) <br>
     * 注释: 或者列名在条件值列表中
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值列表
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orIn(String column, List<T> value) {
        StringBuilder sb = new StringBuilder("and " + column + " in (");
        sb.append("?,".repeat(value.size()));
        sb.deleteCharAt(sb.length() - 1).append(")");
        return new SqlWhereCondition<T>("or " + column + " in ? ", value.toArray());
    }

    /**
     * SQL: [and] column <> value <br>
     * 解释: 并且列名不等于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andNeq(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " <> ? ", value);
    }

    /**
     * SQL: [or] column <> value <br>
     * 解释: 或者列名不等于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orNeq(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " <> ? ", value);
    }

    /**
     * SQL: [and] column < value <br>
     * 解释: 并且列名小于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andLt(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " < ? ", value);
    }

    /**
     * SQL: [or] column < value <br>
     * 解释: 或者列名小于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orLt(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " < ? ", value);
    }

    /**
     * SQL: [and] column <= value <br>
     * 解释: 并且列名小于等于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andLte(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " <= ? ", value);
    }

    /**
     * SQL: [or] column <= value <br>
     * 解释: 或者列名小于等于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orLte(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " <= ? ", value);
    }

    /**
     * SQL: [and] column > value <br>
     * 解释: 并且列名大于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andGt(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " > ? ", value);
    }

    /**
     * SQL: [or] column > value <br>
     * 解释: 或者列名大于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orGt(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " > ? ", value);
    }

    /**
     * SQL: [and] column >= value <br>
     * 解释: 并且列名大于等于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andGte(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " >= ? ", value);
    }

    /**
     * SQL: [or] column >= value <br>
     * 解释: 或者列名大于等于条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orGte(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " >= ? ", value);
    }

    /**
     * SQL: [and] column like value <br>
     * 解释: 并且列名like条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值,通配符需要自己添加
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andLike(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " like ? ", "%" + value + "%");
    }

    /**
     * SQL: [or] column like value <br>
     * 解释: 或者列名like条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值,通配符需要自己添加
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orLike(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " like ? ", "%" + value + "%");
    }

    /**
     * SQL: [and] column not like value <br>
     * 解释: 并且列名not like条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值,通配符需要自己添加
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andNotLike(String column, T value) {
        return new SqlWhereCondition<T>("and " + column + " not like ? ", value);
    }

    /**
     * SQL: [or] column not like value <br>
     * 解释: 或者列名 not like条件值
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @param value  条件值,通配符需要自己添加
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orNotLike(String column, T value) {
        return new SqlWhereCondition<T>("or " + column + " not like ? ", value);
    }

    /**
     * SQL: [and] column is null <br>
     * 解释: 或者列名is null
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andIsNull(String column) {
        return new SqlWhereCondition<T>("and " + column + " is null ");
    }

    /**
     * SQL: [or] column is null <br>
     * 解释: 或者列名is null
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orIsNull(String column) {
        return new SqlWhereCondition<T>("or " + column + " is null ");
    }

    /**
     * SQL: [and] column is not null <br>
     * 解释: 或者列名is not null
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> andIsNotNull(String column) {
        return new SqlWhereCondition<T>("and " + column + " is not null ");
    }

    /**
     * SQL: [or] column is not null <br>
     * 解释: 或者列名is not null
     *
     * @param column 列名,如果存在相同列名则使用表名.列名
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<T> orIsNotNull(String column) {
        return new SqlWhereCondition<T>("or " + column + " is not null ");
    }

//    /**
//     * SQL: [and] column [Syntax]<br>
//     * 解释: 并且列名表达式,比如colum is null
//     *
//     * @param column 列名[与表达式],如果存在相同列名则使用表名.列名
//     * @return {@link SqlWhereCondition}
//     */
//    public static <T> SqlWhereCondition<T> and(String column) {
//        return new SqlWhereCondition<T>("and " + column + " ");
//    }

//    /**
//     * SQL: [or] column [Syntax] <br>
//     * 解释: 或者列名表达式,比如colum is null
//     *
//     * @param column 列名[与表达式],如果存在相同列名则使用表名.列名
//     * @return {@link SqlWhereCondition}
//     */
//    public static <T> SqlWhereCondition<T> or(String column) {
//        return new SqlWhereCondition<T>("or " + column + " ");
//    }

    /**
     * 自定义查询条件:<br>
     * 示例 :<br>
     * prefix=and id in(select tid from table where w=?)<br>
     * value=1<br>
     *
     * @param prefix SQL语句与条件值占位符
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static <T> SqlWhereCondition<?> customCondition(String prefix, T value) {
        return new SqlWhereCondition<Object>(prefix + " ", value);
    }

    /**
     * 自定义查询条件 :<br>
     * 示例 :<br>
     * prefix=and id in(?,?,?)<br>
     * value=1,2,3<br>
     *
     * @param prefix SQL语句与条件值占位符
     * @param value  条件值
     * @return {@link SqlWhereCondition}
     */
    public static SqlWhereCondition<?> customCondition(String prefix, Object... value) {
        return new SqlWhereCondition<>(prefix + " ", value);
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "SqlWhereCondition [require=" + require + ", value=" + value + ", values=" + Arrays.toString(values) + "]";
    }

}
