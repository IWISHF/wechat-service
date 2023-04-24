package com.merkle.wechat.modules.loyalty.vo;

import com.merkle.wechat.common.annotation.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

public class DigikeyCampaignVideoVo {
    @NotEmpty
    @ApiModelProperty(value = "openid 非空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
