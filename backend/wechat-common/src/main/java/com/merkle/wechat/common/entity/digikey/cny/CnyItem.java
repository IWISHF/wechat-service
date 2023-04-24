package com.merkle.wechat.common.entity.digikey.cny;

import java.util.Date;

import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_cny_item")
public class CnyItem extends BaseEntity {
    private String name;
    private String description;
    private String openedPicUrl;
    private String unopenExpiredPicUrl;
    private String unopenCurrentPicUrl;
    private String unopenFuturePicUrl;
    private String pageUrl;
    private Date enableStartDate;

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

    public String getOpenedPicUrl() {
        return openedPicUrl;
    }

    public void setOpenedPicUrl(String openedPicUrl) {
        this.openedPicUrl = openedPicUrl;
    }

    public String getUnopenExpiredPicUrl() {
        return unopenExpiredPicUrl;
    }

    public void setUnopenExpiredPicUrl(String unopenExpiredPicUrl) {
        this.unopenExpiredPicUrl = unopenExpiredPicUrl;
    }

    public String getUnopenCurrentPicUrl() {
        return unopenCurrentPicUrl;
    }

    public void setUnopenCurrentPicUrl(String unopenCurrentPicUrl) {
        this.unopenCurrentPicUrl = unopenCurrentPicUrl;
    }

    public String getUnopenFuturePicUrl() {
        return unopenFuturePicUrl;
    }

    public void setUnopenFuturePicUrl(String unopenFuturePicUrl) {
        this.unopenFuturePicUrl = unopenFuturePicUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Date getEnableStartDate() {
        return enableStartDate;
    }

    public void setEnableStartDate(Date enableStartDate) {
        this.enableStartDate = enableStartDate;
    }

}
