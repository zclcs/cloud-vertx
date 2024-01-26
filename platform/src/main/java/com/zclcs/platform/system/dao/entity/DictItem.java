package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.Column;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字典 Entity
 *
 * @author zclcs
 * @since 2023-09-01 20:03:54.686
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
public class DictItem extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Column(name = "id")
    private Long id;

    /**
     * 字典key
     */
    @Column(name = "dict_name")
    private String dictName;

    /**
     * 父级字典值
     */
    @Column(name = "parent_value")
    private String parentValue;

    /**
     * 值
     */
    @Column(name = "value")
    private String value;

    /**
     * 标签
     */
    @Column(name = "title")
    private String title;

    /**
     * 字典类型 @@system_dict_item.type
     */
    @Column(name = "type")
    private String type;

    /**
     * 是否系统字典 @@yes_no
     */
    @Column(name = "whether_system_dict")
    private String whetherSystemDict;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 排序（升序）
     */
    @Column(name = "sorted")
    private Integer sorted;

    /**
     * 是否禁用 @@yes_no
     */
    @Column(name = "is_disabled")
    private String isDisabled;

    public DictItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getParentValue() {
        return parentValue;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWhetherSystemDict() {
        return whetherSystemDict;
    }

    public void setWhetherSystemDict(String whetherSystemDict) {
        this.whetherSystemDict = whetherSystemDict;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSorted() {
        return sorted;
    }

    public void setSorted(Integer sorted) {
        this.sorted = sorted;
    }

    public String getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        this.isDisabled = isDisabled;
    }
}