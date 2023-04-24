package com.merkle.wechat.modules.aia.vo;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.merkle.wechat.common.annotation.NotEmpty;

public class SendCaptchaVo {
    @NotEmpty
    private String openid;

    @NotEmpty
    @Pattern(regexp = "[0-9]{11}")
    @Length(min = 11, max = 11)
    private String phone;
    private Long channelId;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

}
