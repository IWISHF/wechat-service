package com.merkle.wechat.modules.digikey.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CMSSearchVo {
    @ApiModelProperty(required = false)
    private String keywords = "";
    @ApiModelProperty(required = false, value = "start from 1")
    private String currentPage = "1";
    @ApiModelProperty(required = false)
    private String pageSize = "10";

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

}
