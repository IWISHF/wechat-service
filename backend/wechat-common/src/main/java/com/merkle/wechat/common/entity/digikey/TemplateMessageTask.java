package com.merkle.wechat.common.entity.digikey;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "digikey_template_message_task")
public class TemplateMessageTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String templateId;
    private String first;
    private String remark;
    private String redirectUrl;
    private int taskKeywordsLength;
    private String keyword1name;
    private String keyword2name;
    private String keyword3name;
    private String keyword4name;
    private Date createdDate = new Date();
    private Date sendDate = new Date();

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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public int getTaskKeywordsLength() {
        return taskKeywordsLength;
    }

    public void setTaskKeywordsLength(int taskKeywordsLength) {
        this.taskKeywordsLength = taskKeywordsLength;
    }

    public String getKeyword1name() {
        return keyword1name;
    }

    public void setKeyword1name(String keyword1name) {
        this.keyword1name = keyword1name;
    }

    public String getKeyword2name() {
        return keyword2name;
    }

    public void setKeyword2name(String keyword2name) {
        this.keyword2name = keyword2name;
    }

    public String getKeyword3name() {
        return keyword3name;
    }

    public void setKeyword3name(String keyword3name) {
        this.keyword3name = keyword3name;
    }

    public String getKeyword4name() {
        return keyword4name;
    }

    public void setKeyword4name(String keyword4name) {
        this.keyword4name = keyword4name;
    }

}
