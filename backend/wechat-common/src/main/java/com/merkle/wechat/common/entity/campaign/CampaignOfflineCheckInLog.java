package com.merkle.wechat.common.entity.campaign;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.merkle.wechat.common.annotation.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "campaign_offline_checkin_log")
public class CampaignOfflineCheckInLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "not required")
    private Long id;

    @NotEmpty
    @ApiModelProperty(required = true)
    private String openid;

    @NotNull
    @ApiModelProperty(required = true)
    private Long campaignId;

    @NotEmpty
    @ApiModelProperty(required = true)
    private String campaignTitle;

    @NotNull
    @ApiModelProperty(required = true)
    private Long wechatPublicNoId;

    @ApiModelProperty(value = "not required")
    private Date createdDate = new Date();

    @ApiModelProperty(value = "not required")
    private boolean loyaltyOfflineEventSyncStatus = false;

    @ApiModelProperty(value = "not required")
    private boolean templateMessageSendStatus = false;

    @ApiModelProperty(value = "not required")
    private String templateErrorMessage;

    private String unionid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isLoyaltyOfflineEventSyncStatus() {
        return loyaltyOfflineEventSyncStatus;
    }

    public void setLoyaltyOfflineEventSyncStatus(boolean loyaltyOfflineEventSyncStatus) {
        this.loyaltyOfflineEventSyncStatus = loyaltyOfflineEventSyncStatus;
    }

    public boolean isTemplateMessageSendStatus() {
        return templateMessageSendStatus;
    }

    public void setTemplateMessageSendStatus(boolean templateMessageSendStatus) {
        this.templateMessageSendStatus = templateMessageSendStatus;
    }

    public String getTemplateErrorMessage() {
        return templateErrorMessage;
    }

    public void setTemplateErrorMessage(String templateErrorMessage) {
        this.templateErrorMessage = templateErrorMessage;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

}
