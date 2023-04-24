package com.merkle.wechat.modules.digikey.vo;

import javax.validation.constraints.NotNull;

import com.merkle.wechat.common.annotation.NotEmpty;

public class LightUpVo {
    @NotEmpty
    private String openid;
    @NotNull
    private Long wishItemId;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getWishItemId() {
        return wishItemId;
    }

    public void setWishItemId(Long wishItemId) {
        this.wishItemId = wishItemId;
    }

}
