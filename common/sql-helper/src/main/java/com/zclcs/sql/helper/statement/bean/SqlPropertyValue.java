package com.zclcs.sql.helper.statement.bean;

/**
 * 用于存储数据库列名,与列名对应的值
 *
 * @author <a href="https://zclcstools.org/">zclcs</a>
 */
public class SqlPropertyValue<T> {
    /**
     * 列名
     */
    private String name;
    /**
     * 参数值
     */
    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public SqlPropertyValue(String name, T value) {
        super();
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SqlPropertyValue [name=" + name + ", value=" + value + "]";
    }

}
