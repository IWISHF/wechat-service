package com.merkle.wechat.modules.digikey.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CnyItemVo {
    private Long id;
    private String name;
    private String description;
    private String openedPicUrl;
    private String unopenExpiredPicUrl;
    private String unopenCurrentPicUrl;
    private String unopenFuturePicUrl;
    private String pageUrl;
    private Date enableStartDate;
    private Date openDate;
    @ApiModelProperty(value = "opened_with_points,opened_without_points,unopened_expired,unopened_current,unopened_future")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
