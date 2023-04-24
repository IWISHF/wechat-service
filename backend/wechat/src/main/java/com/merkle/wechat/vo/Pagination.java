package com.merkle.wechat.vo;

import java.util.List;

import org.springframework.data.domain.Page;

public class Pagination<T> {
    private int pageSize;
    private Long totalAmount;
    private int currentPage;
    private List<T> result;
    private String sort;

    public Pagination(Page<T> page) {
        this.pageSize = page.getSize();
        this.totalAmount = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.result = page.getContent();
        if (page.getSort() != null) {
            this.sort = page.getSort().toString();
        }
    }

    public Pagination() {
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
