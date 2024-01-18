package com.zclcs.cloud.core.base;

/**
 * 分页实体
 *
 * @author zclcs
 */
public class PageAo {

    /**
     * 页码
     * 默认：1
     */
    private Long pageNum = 1L;

    /**
     * 页数
     * 默认：10
     */
    private Long pageSize = 10L;

    public PageAo() {
    }

    public PageAo(Long pageNum, Long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
