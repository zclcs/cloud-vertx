package com.zclcs.platform.system.dao.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字典树
 *
 * @author zclcs
 * @since 2023-03-06 10:56:41.301
 */
public class DictItemTreeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典类型 @@system_dict_item.type
     */
    private String type;

    /**
     * 字典类型 @@system_dict_item.type
     */
    private String typeText;

    /**
     * 是否系统字典 @@yes_no
     */
    private String whetherSystemDict;

    /**
     * 是否系统字典 @@yes_no
     */
    private String whetherSystemDictText;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序（升序）
     */
    private Integer sorted;

    public DictItemTreeVo() {
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
}