package com.merkle.wechat.modules.digikey.vo;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.Range;

import com.merkle.wechat.common.annotation.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DigikeyKeywordSearchVo {
    @NotEmpty
    @ApiModelProperty(required = true)
    private String keyword;
    private DigikeySort sort = new DigikeySort();
    @Range(min = 1)
    private int currentPage = 1;
    @Range(min = 1, max = 50)
    private int pageSize = 50;
    @ApiModelProperty(required = false, value = "数组[]", allowableValues = "LeadFree," + "ChipOutpostOnly,"
            + "CollapsePackingTypes," + "ExcludeChipOutpost," + "ExcludeNonStock," + "Has3DModel," + "InStock,"
            + "ManufacturerPartSearch," + "NewProductsOnly," + "PbFreeOnly," + "RoHSCompliant")
    private String[] searchOptions = new String[] {};
    private Map<String, int[]> filters = new HashMap<>();
    private String currency = "CNY";

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public DigikeySort getSort() {
        return sort;
    }

    public void setSort(DigikeySort sort) {
        this.sort = sort;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(String[] searchOptions) {
        this.searchOptions = searchOptions;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Map<String, int[]> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, int[]> filters) {
        this.filters = filters;
    }

}
