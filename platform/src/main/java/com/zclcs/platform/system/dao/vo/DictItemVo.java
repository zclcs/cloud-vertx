package com.zclcs.platform.system.dao.vo;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字典 Vo
 *
 * @author zclcs
 * @since 2023-09-01 20:03:54.686
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
public class DictItemVo extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * 默认值：
     */
    private Long id;

    /**
     * 字典key
     * 默认值：
     */
    private String dictName;

    /**
     * 父级字典值
     * 默认值：0
     */
    private String parentValue;

    /**
     * 值
     * 默认值：
     */
    private String value;

    /**
     * 标签
     * 默认值：
     */
    private String title;

    /**
     * 字典类型 @@system_dict_item.type
     * 默认值：0
     */
    private String type;

    /**
     * 字典类型 @@system_dict_item.type
     */
    private String typeText;

    /**
     * 是否系统字典 @@yes_no
     * 默认值：1
     */
    private String whetherSystemDict;

    /**
     * 是否系统字典 @@yes_no
     */
    private String whetherSystemDictText;

    /**
     * 描述
     * 默认值：
     */
    private String description;

    /**
     * 排序（升序）
     * 默认值：0
     */
    private Integer sorted;

    /**
     * 是否禁用 @@yes_no
     * 默认值：0
     */
    private String isDisabled;

    /**
     * 是否禁用 @@yes_no
     */
    private String isDisabledText;

    public DictItemVo() {
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

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getWhetherSystemDict() {
        return whetherSystemDict;
    }

    public void setWhetherSystemDict(String whetherSystemDict) {
        this.whetherSystemDict = whetherSystemDict;
    }

    public String getWhetherSystemDictText() {
        return whetherSystemDictText;
    }

    public void setWhetherSystemDictText(String whetherSystemDictText) {
        this.whetherSystemDictText = whetherSystemDictText;
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

    public String getIsDisabledText() {
        return isDisabledText;
    }

    public void setIsDisabledText(String isDisabledText) {
        this.isDisabledText = isDisabledText;
    }
}