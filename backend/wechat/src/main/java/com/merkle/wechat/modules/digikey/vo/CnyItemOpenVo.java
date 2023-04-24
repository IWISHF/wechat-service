package com.merkle.wechat.modules.digikey.vo;

import javax.validation.constraints.NotNull;

import com.merkle.wechat.common.annotation.NotEmpty;

public class CnyItemOpenVo {
    @NotEmpty
    private String openid;
    @NotNull
    private Long itemId;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

}
