package com.zclcs.cloud.core.base;

import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 *
 * @author zclcs
 */
public class Page<T> {

    /**
     * 分页对象记录列表
     */
    protected List<T> list;

    /**
     * 总条数
     */
    protected Long total;

    /**
     * 每页显示条数
     */
    protected Long pageSize;

    /**
     * 当前页
     */
    protected Long pageNum;

    /**
     * 总页数
     */
    protected Long pages;

    public Page() {
        this(1L, 10L, 0L);
    }

    public Page(long pageNum, long pageSize) {
        this(pageNum, pageSize, 0L);
    }

    public Page(long pageNum, long pageSize, long total) {
        this(pageNum, pageSize, total, Collections.emptyList());
    }

    public Page(Long pageNum, Long pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = total == 0 ? 0L : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        this.list = list;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }
}
