package com.zclcs.platform.system.dao.entity;

import com.zclcs.cloud.lib.domain.entity.BaseEntity;
import com.zclcs.sql.helper.annotation.Table;
import com.zclcs.sql.helper.annotation.TableColumn;
import com.zclcs.sql.helper.annotation.TableId;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.format.SnakeCase;
import io.vertx.sqlclient.templates.annotations.RowMapped;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单 Entity
 *
 * @author zclcs
 * @since 2023-09-01 20:05:00.313
 */
@DataObject
@RowMapped(formatter = SnakeCase.class)
@Table(name = "system_menu")
public class Menu extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 目录/菜单/按钮id
     */
    @TableId(name = "menu_id")
    private Long menuId;

    /**
     * 目录/菜单/按钮编码（唯一值）
     */
    @TableColumn(name = "menu_code")
    private String menuCode;

    /**
     * 上级目录/菜单编码
     */
    @TableColumn(name = "parent_code")
    private String parentCode;

    /**
     * 目录/菜单/按钮名称
     */
    @TableColumn(name = "menu_name")
    private String menuName;

    /**
     * 页面缓存名称
     */
    @TableColumn(name = "keep_alive_name")
    private String keepAliveName;

    /**
     * 对应路由path
     */
    @TableColumn(name = "path")
    private String path;

    /**
     * 对应路由组件component
     */
    @TableColumn(name = "component")
    private String component;

    /**
     * 打开目录重定向的子菜单
     */
    @TableColumn(name = "redirect")
    private String redirect;

    /**
     * 权限标识
     */
    @TableColumn(name = "perms")
    private String perms;

    /**
     * 图标
     */
    @TableColumn(name = "icon")
    private String icon;

    /**
     * 类型 @@system_menu.type
     */
    @TableColumn(name = "type")
    private String type;

    /**
     * 是否隐藏菜单 @@yes_no
     */
    @TableColumn(name = "hide_menu")
    private String hideMenu;

    /**
     * 是否忽略KeepAlive缓存 @@yes_no
     */
    @TableColumn(name = "ignore_keep_alive")
    private String ignoreKeepAlive;

    /**
     * 隐藏该路由在面包屑上面的显示 @@yes_no
     */
    @TableColumn(name = "hide_breadcrumb")
    private String hideBreadcrumb;

    /**
     * 隐藏所有子菜单 @@yes_no
     */
    @TableColumn(name = "hide_children_in_menu")
    private String hideChildrenInMenu;

    /**
     * 当前激活的菜单。用于配置详情页时左侧激活的菜单路径
     */
    @TableColumn(name = "current_active_menu")
    private String currentActiveMenu;

    /**
     * 排序
     */
    @TableColumn(name = "order_num")
    private Double orderNum;

    public Menu() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getKeepAliveName() {
        return keepAliveName;
    }

    public void setKeepAliveName(String keepAliveName) {
        this.keepAliveName = keepAliveName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHideMenu() {
        return hideMenu;
    }

    public void setHideMenu(String hideMenu) {
        this.hideMenu = hideMenu;
    }

    public String getIgnoreKeepAlive() {
        return ignoreKeepAlive;
    }

    public void setIgnoreKeepAlive(String ignoreKeepAlive) {
        this.ignoreKeepAlive = ignoreKeepAlive;
    }

    public String getHideBreadcrumb() {
        return hideBreadcrumb;
    }

    public void setHideBreadcrumb(String hideBreadcrumb) {
        this.hideBreadcrumb = hideBreadcrumb;
    }

    public String getHideChildrenInMenu() {
        return hideChildrenInMenu;
    }

    public void setHideChildrenInMenu(String hideChildrenInMenu) {
        this.hideChildrenInMenu = hideChildrenInMenu;
    }

    public String getCurrentActiveMenu() {
        return currentActiveMenu;
    }

    public void setCurrentActiveMenu(String currentActiveMenu) {
        this.currentActiveMenu = currentActiveMenu;
    }

    public Double getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Double orderNum) {
        this.orderNum = orderNum;
    }
}