package com.zclcs.platform.system.dao.cache;

import com.zclcs.platform.system.dao.entity.Menu;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单 CacheVo
 *
 * @author zclcs
 * @since 2023-09-01 20:05:00.313
 */
public class MenuCacheVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 目录/菜单/按钮id
     */
    private Long menuId;

    /**
     * 目录/菜单/按钮编码（唯一值）
     */
    private String menuCode;

    /**
     * 上级目录/菜单编码
     */
    private String parentCode;

    /**
     * 目录/菜单/按钮名称
     */
    private String menuName;

    /**
     * 页面缓存名称
     */
    private String keepAliveName;

    /**
     * 对应路由path
     */
    private String path;

    /**
     * 对应路由组件component
     */
    private String component;

    /**
     * 打开目录重定向的子菜单
     */
    private String redirect;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型 @@system_menu.type
     */
    private String type;

    /**
     * 类型 @@system_menu.type
     */
    private String typeText;

    /**
     * 是否隐藏菜单 @@yes_no
     */
    private String hideMenu;

    /**
     * 是否隐藏菜单 @@yes_no
     */
    private String hideMenuText;

    /**
     * 是否忽略KeepAlive缓存 @@yes_no
     */
    private String ignoreKeepAlive;

    /**
     * 是否忽略KeepAlive缓存 @@yes_no
     */
    private String ignoreKeepAliveText;

    /**
     * 隐藏该路由在面包屑上面的显示 @@yes_no
     */
    private String hideBreadcrumb;

    /**
     * 隐藏该路由在面包屑上面的显示 @@yes_no
     */
    private String hideBreadcrumbText;

    /**
     * 隐藏所有子菜单 @@yes_no
     */
    private String hideChildrenInMenu;

    /**
     * 隐藏所有子菜单 @@yes_no
     */
    private String hideChildrenInMenuText;

    /**
     * 当前激活的菜单。用于配置详情页时左侧激活的菜单路径
     */
    private String currentActiveMenu;

    /**
     * 排序
     */
    private Double orderNum;

    public MenuCacheVo() {
    }

    public MenuCacheVo(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuCode = menu.getMenuCode();
        this.parentCode = menu.getParentCode();
        this.menuName = menu.getMenuName();
        this.keepAliveName = menu.getKeepAliveName();
        this.path = menu.getPath();
        this.component = menu.getComponent();
        this.redirect = menu.getRedirect();
        this.perms = menu.getPerms();
        this.icon = menu.getIcon();
        this.type = menu.getType();
        this.hideMenu = menu.getHideMenu();
        this.ignoreKeepAlive = menu.getIgnoreKeepAlive();
        this.hideBreadcrumb = menu.getHideBreadcrumb();
        this.hideChildrenInMenu = menu.getHideChildrenInMenu();
        this.currentActiveMenu = menu.getCurrentActiveMenu();
        this.orderNum = menu.getOrderNum();
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

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getHideMenu() {
        return hideMenu;
    }

    public void setHideMenu(String hideMenu) {
        this.hideMenu = hideMenu;
    }

    public String getHideMenuText() {
        return hideMenuText;
    }

    public void setHideMenuText(String hideMenuText) {
        this.hideMenuText = hideMenuText;
    }

    public String getIgnoreKeepAlive() {
        return ignoreKeepAlive;
    }

    public void setIgnoreKeepAlive(String ignoreKeepAlive) {
        this.ignoreKeepAlive = ignoreKeepAlive;
    }

    public String getIgnoreKeepAliveText() {
        return ignoreKeepAliveText;
    }

    public void setIgnoreKeepAliveText(String ignoreKeepAliveText) {
        this.ignoreKeepAliveText = ignoreKeepAliveText;
    }

    public String getHideBreadcrumb() {
        return hideBreadcrumb;
    }

    public void setHideBreadcrumb(String hideBreadcrumb) {
        this.hideBreadcrumb = hideBreadcrumb;
    }

    public String getHideBreadcrumbText() {
        return hideBreadcrumbText;
    }

    public void setHideBreadcrumbText(String hideBreadcrumbText) {
        this.hideBreadcrumbText = hideBreadcrumbText;
    }

    public String getHideChildrenInMenu() {
        return hideChildrenInMenu;
    }

    public void setHideChildrenInMenu(String hideChildrenInMenu) {
        this.hideChildrenInMenu = hideChildrenInMenu;
    }

    public String getHideChildrenInMenuText() {
        return hideChildrenInMenuText;
    }

    public void setHideChildrenInMenuText(String hideChildrenInMenuText) {
        this.hideChildrenInMenuText = hideChildrenInMenuText;
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