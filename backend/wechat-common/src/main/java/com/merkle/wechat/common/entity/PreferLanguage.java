package com.merkle.wechat.common.entity;

import javax.persistence.Entity;

import com.merkle.wechat.common.annotation.NotEmpty;

@Entity(name = "prefer_language")
public class PreferLanguage extends BaseEntity {
    @NotEmpty
    private String openid;
    @NotEmpty
    private String language;
    private Long channelId;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

}
