package com.merkle.wechat.vo.statistics;

import org.springframework.data.domain.Page;

public class StatisticsPage {
    private int pageSize;
    private Long totalAmount;
    private int currentPage;
    private String sort;

    @SuppressWarnings("rawtypes")
    public StatisticsPage(Page page) {
        this.pageSize = page.getSize();
        this.totalAmount = page.getTotalElements();
        this.currentPage = page.getNumber();
        if (page.getSort() != null) {
            this.sort = page.getSort().toString();
        }
    }

    public StatisticsPage() {

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
