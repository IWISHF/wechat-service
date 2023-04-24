package com.merkle.wechat.common.entity.campaign;

import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "campaign_answer")
public class CampaignAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long campaignAnswerId;
    // CampaignId
    @JsonProperty("id")
    private Long campaignId;
    private String openid;
    private String unionid;
    private String title;
    private String subTitle;
    private String description;
    private String descriptionHtml;
    private Long wechatPublicNoId;
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "campaignAnswerId")
    @OrderBy("show_order ASC")
    private Set<QuestionAnswer> questions;
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "campaignId")
    @OrderBy("show_order ASC")
    private Set<TermAnswer> terms;
    private Date createdDate = new Date();

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getCampaignAnswerId() {
        return campaignAnswerId;
    }

    public void setCampaignAnswerId(Long campaignAnswerId) {
        this.campaignAnswerId = campaignAnswerId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Set<QuestionAnswer> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionAnswer> questions) {
        this.questions = questions;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public void setDescriptionHtml(String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public Set<TermAnswer> getTerms() {
        return terms;
    }

    public void setTerms(Set<TermAnswer> terms) {
        this.terms = terms;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

}
