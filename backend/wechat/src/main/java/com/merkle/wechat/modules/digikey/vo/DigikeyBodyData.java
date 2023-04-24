package com.merkle.wechat.modules.digikey.vo;

import java.util.HashMap;
import java.util.Map;

public class DigikeyBodyData {

    private String[] searchOptions = new String[] {};
    private String keywords;
    private int recordCount;
    private int recordStartPosition;
    private Map<String, int[]> filters = new HashMap<>();
    private DigikeySort sort = new DigikeySort();
    private int requestedQuantity = 50;

    public String[] getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(String[] searchOptions) {
        this.searchOptions = searchOptions;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getRecordStartPosition() {
        return recordStartPosition;
    }

    public void setRecordStartPosition(int recordStartPosition) {
        this.recordStartPosition = recordStartPosition;
    }

    public Map<String, int[]> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, int[]> filters) {
        this.filters = filters;
    }

    public DigikeySort getSort() {
        return sort;
    }

    public void setSort(DigikeySort sort) {
        this.sort = sort;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(int requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

}
