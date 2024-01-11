package com.zclcs.platform.system.dao.router;

import java.io.Serial;
import java.io.Serializable;

/**
 * Vue路由 Meta
 *
 * @author zclcs
 */
public class RouterMeta implements Serializable {

    @Serial
    private static final long serialVersionUID = 5499925008927195914L;

    private String title;
    private String icon;
    private Boolean hideMenu;
    private Boolean ignoreKeepAlive;
    private Boolean hideBreadcrumb;
    private Boolean hideChildrenInMenu;
    private String currentActiveMenu;

    public RouterMeta() {
    }

    public RouterMeta(String title, String icon, Boolean hideMenu, Boolean ignoreKeepAlive, Boolean hideBreadcrumb, Boolean hideChildrenInMenu, String currentActiveMenu) {
        this.title = title;
        this.icon = icon;
        this.hideMenu = hideMenu;
        this.ignoreKeepAlive = ignoreKeepAlive;
        this.hideBreadcrumb = hideBreadcrumb;
        this.hideChildrenInMenu = hideChildrenInMenu;
        this.currentActiveMenu = currentActiveMenu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getHideMenu() {
        return hideMenu;
    }

    public void setHideMenu(Boolean hideMenu) {
        this.hideMenu = hideMenu;
    }

    public Boolean getIgnoreKeepAlive() {
        return ignoreKeepAlive;
    }

    public void setIgnoreKeepAlive(Boolean ignoreKeepAlive) {
        this.ignoreKeepAlive = ignoreKeepAlive;
    }

    public Boolean getHideBreadcrumb() {
        return hideBreadcrumb;
    }

    public void setHideBreadcrumb(Boolean hideBreadcrumb) {
        this.hideBreadcrumb = hideBreadcrumb;
    }

    public Boolean getHideChildrenInMenu() {
        return hideChildrenInMenu;
    }

    public void setHideChildrenInMenu(Boolean hideChildrenInMenu) {
        this.hideChildrenInMenu = hideChildrenInMenu;
    }

    public String getCurrentActiveMenu() {
        return currentActiveMenu;
    }

    public void setCurrentActiveMenu(String currentActiveMenu) {
        this.currentActiveMenu = currentActiveMenu;
    }
}
