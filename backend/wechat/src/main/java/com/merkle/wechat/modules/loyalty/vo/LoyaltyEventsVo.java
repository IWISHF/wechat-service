package com.merkle.wechat.modules.loyalty.vo;

import com.merkle.wechat.common.annotation.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

public class LoyaltyEventsVo {
    @ApiModelProperty(value = "openid")
    @NotEmpty
    private String id;
    @ApiModelProperty(value = "可以指定查询某种类型事件，如purchase")
    private String type;
    @NotEmpty
    @ApiModelProperty(value = "当前页")
    private String pageNumber;
    @NotEmpty
    @ApiModelProperty(value = "每页显示数量")
    private String pageSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
