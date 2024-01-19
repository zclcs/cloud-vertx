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
    private Long pageNum;

    /**
     * 页数
     * 默认：10
     */
    private Long pageSize;

    public PageAo() {
        this.pageNum = 1L;
        this.pageSize = 10L;
    }

    public PageAo(Long pageNum, Long pageSize) {
        this.pageNum = pageNum == null ? 1L : pageNum;
        this.pageSize = pageSize == null ? 10L : pageSize;
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

    public Long getSqlQueryStart() {
        return (pageNum - 1) * pageSize;
    }

    public Long getSqlQueryEnd() {
        return pageSize;
    }
}
