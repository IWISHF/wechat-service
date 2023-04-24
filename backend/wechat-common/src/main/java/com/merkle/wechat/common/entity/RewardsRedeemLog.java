package com.merkle.wechat.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.merkle.wechat.common.annotation.NotEmpty;

@Entity(name = "rewards_redeem_log")
public class RewardsRedeemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String openid;

    private String unionid;

    @NotEmpty
    private String rewardId;

    @NotEmpty
    private String rewardName;

    private String rewardGroup;

    private String rewardType;

    private String phone;

    private String name;

    private String address;

    private boolean redeemStatus;

    @Column(columnDefinition = "TEXT")
    private String redeemResponse;

    private boolean templateMessageSendStatus = false;

    private String templateMessageErrorMessage;

    private String couponCode;

    private String expressCompany;

    private String trackingCode;

    private boolean expressTemplateMessageSendStatus = false;

    private String expressTemplateMessageErrorMessage;

    private Date expressSendedDate;

    private String pubNoAppId;

    private boolean checkStatus;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

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

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(boolean redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public String getRedeemResponse() {
        return redeemResponse;
    }

    public void setRedeemResponse(String redeemResponse) {
        this.redeemResponse = redeemResponse;
    }

    public boolean isTemplateMessageSendStatus() {
        return templateMessageSendStatus;
    }

    public void setTemplateMessageSendStatus(boolean templateMessageSendStatus) {
        this.templateMessageSendStatus = templateMessageSendStatus;
    }

    public String getPubNoAppId() {
        return pubNoAppId;
    }

    public void setPubNoAppId(String pubNoAppId) {
        this.pubNoAppId = pubNoAppId;
    }

    public String getTemplateMessageErrorMessage() {
        return templateMessageErrorMessage;
    }

    public void setTemplateMessageErrorMessage(String templateMessageErrorMessage) {
        this.templateMessageErrorMessage = templateMessageErrorMessage;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public boolean getExpressTemplateMessageSendStatus() {
        return expressTemplateMessageSendStatus;
    }

    public void setExpressTemplateMessageSendStatus(boolean expressTemplateMessageSendStatus) {
        this.expressTemplateMessageSendStatus = expressTemplateMessageSendStatus;
    }

    public String getExpressTemplateMessageErrorMessage() {
        return expressTemplateMessageErrorMessage;
    }

    public void setExpressTemplateMessageErrorMessage(String expressTemplateMessageErrorMessage) {
        this.expressTemplateMessageErrorMessage = expressTemplateMessageErrorMessage;
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public Date getExpressSendedDate() {
        return expressSendedDate;
    }

    public void setExpressSendedDate(Date expressSendedDate) {
        this.expressSendedDate = expressSendedDate;
    }

    public String getRewardGroup() {
        return rewardGroup;
    }

    public void setRewardGroup(String rewardGroup) {
        this.rewardGroup = rewardGroup;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

}
