package com.merkle.wechat.common.entity.digikey.wish;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_wish_item")
public class WishItem extends BaseEntity {
    private String name;
    private String description;
    private String picUrl;
    private String pageUrl;
    private Date enableStartDate;
    private Long campaignId;
    @Lob
    private String customData;

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Date getEnableStartDate() {
        return enableStartDate;
    }

    public void setEnableStartDate(Date enableStartDate) {
        this.enableStartDate = enableStartDate;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

}
