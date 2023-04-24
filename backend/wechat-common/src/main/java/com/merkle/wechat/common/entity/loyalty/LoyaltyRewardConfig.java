package com.merkle.wechat.common.entity.loyalty;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.merkle.wechat.common.annotation.NotEmpty;
import com.merkle.wechat.common.entity.template.WeixinExpressTemplateConfig;
import com.merkle.wechat.common.entity.template.WeixinNoticeTemplateConfig;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "loyalty_reward_config")
public class LoyaltyRewardConfig {
    @Transient
    public static final String IN_KIND = "0";
    public static final String VIRTUAL = "1";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty("创建不必须")
    private Long id;

    @NotNull
    @ApiModelProperty("必须")
    private int rewardId;

    @NotEmpty
    @ApiModelProperty("必须")
    private String rewardName;

    @Pattern(regexp = "^[0,1]")
    @ApiModelProperty(allowableValues = "0,1", value = "1 is virtual rewards, 0 is in-kind incentives", required = true)
    private String rewardType;

    // all type must have this
    @NotEmpty
    @ApiModelProperty("必须")
    private String noticeTemplateId;

    // all type must have this
    @NotEmpty
    @ApiModelProperty("必须")
    @OrderBy("orderIndex asc")
    @JoinColumn(name = "loyaltyRewardConfigId")
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<WeixinNoticeTemplateConfig> noticeTemplateConfigs = new HashSet<>();

    @ApiModelProperty("实体必须")
    private String expressTemplateId;

    @OrderBy("orderIndex asc")
    @ApiModelProperty("实体必须")
    @JoinColumn(name = "loyaltyRewardConfigId")
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<WeixinExpressTemplateConfig> expressTemplateConfigs = new HashSet<>();

    @ApiModelProperty("必须")
    private boolean enable = false;

    @ApiModelProperty("不必须")
    private Long wechatPublicNoId;

    @ApiModelProperty("不必须")
    private String appId;

    @ApiModelProperty("实体必须")
    private String expressCompany;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
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

    public String getNoticeTemplateId() {
        return noticeTemplateId;
    }

    public void setNoticeTemplateId(String noticeTemplateId) {
        this.noticeTemplateId = noticeTemplateId;
    }

    public Set<WeixinNoticeTemplateConfig> getNoticeTemplateConfigs() {
        return noticeTemplateConfigs;
    }

    public void setNoticeTemplateConfigs(Set<WeixinNoticeTemplateConfig> noticeTemplateConfigs) {
        this.noticeTemplateConfigs = noticeTemplateConfigs;
    }

    public String getExpressTemplateId() {
        return expressTemplateId;
    }

    public void setExpressTemplateId(String expressTemplateId) {
        this.expressTemplateId = expressTemplateId;
    }

    public Set<WeixinExpressTemplateConfig> getExpressTemplateConfigs() {
        return expressTemplateConfigs;
    }

    public void setExpressTemplateConfigs(Set<WeixinExpressTemplateConfig> expressTemplateConfigs) {
        this.expressTemplateConfigs = expressTemplateConfigs;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

}
