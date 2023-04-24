package com.merkle.wechat.common.entity.survey;

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
import com.merkle.wechat.common.entity.campaign.QuestionAnswer;
import com.merkle.wechat.common.entity.campaign.TermAnswer;

@Entity(name = "surveyanswer")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long surveyAnswerId;
    // CampaignId
    @JsonProperty("id")
    private Long surveyId;
    private String openid;
    private String title;
    private String subTitle;
    private String description;
    private String descriptionHtml;
    private String coverUrl;
    private Long wechatPublicNoId;
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "surveyAnswerId")
    @OrderBy("show_order ASC")
    private Set<QuestionAnswer> questions;
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "surveyId")
    @OrderBy("show_order ASC")
    private Set<TermAnswer> terms;
    private Date createdDate = new Date();
    private Date updatedDate = new Date();
    private String eventType;
    private boolean eventRecordStatus;
    private String eventRecordResponse;

    public Long getSurveyAnswerId() {
        return surveyAnswerId;
    }

    public void setSurveyAnswerId(Long surveyAnswerId) {
        this.surveyAnswerId = surveyAnswerId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public Set<QuestionAnswer> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionAnswer> questions) {
        this.questions = questions;
    }

    public Set<TermAnswer> getTerms() {
        return terms;
    }

    public void setTerms(Set<TermAnswer> terms) {
        this.terms = terms;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isEventRecordStatus() {
        return eventRecordStatus;
    }

    public void setEventRecordStatus(boolean eventRecordStatus) {
        this.eventRecordStatus = eventRecordStatus;
    }

    public String getEventRecordResponse() {
        return eventRecordResponse;
    }

    public void setEventRecordResponse(String eventRecordResponse) {
        this.eventRecordResponse = eventRecordResponse;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

}
