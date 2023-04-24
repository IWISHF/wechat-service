package com.merkle.wechat.vo.batch;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.merkle.wechat.common.entity.TagGroupCondition;
import com.merkle.wechat.common.entity.batch.BatchTaskError;
import com.merkle.wechat.vo.autoreply.AutoReplyRuleVo;

public class BatchTaskVo {
    private Long id;
    private String name;
    private String description;

    private String progressStatus;
    private boolean triggerNow = false;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date triggerDate;

    // filterConditions
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private Date executeDate;
    private int sex;

    private Set<TagGroupCondition> groupConditions = new HashSet<>();

    @SuppressWarnings("rawtypes")
    private AutoReplyRuleVo rule;

    private Set<BatchTaskError> errors = new HashSet<>();

    private Integer totalCount = 0;
    private Integer filterCount = 0;
    private Integer sentCount = 0;
    private Integer errorCount = 0;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

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

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    public boolean isTriggerNow() {
        return triggerNow;
    }

    public void setTriggerNow(boolean triggerNow) {
        this.triggerNow = triggerNow;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Set<TagGroupCondition> getGroupConditions() {
        return groupConditions;
    }

    public void setGroupConditions(Set<TagGroupCondition> groupConditions) {
        this.groupConditions = groupConditions;
    }

    @SuppressWarnings("rawtypes")
    public AutoReplyRuleVo getRule() {
        return rule;
    }

    @SuppressWarnings("rawtypes")
    public void setRule(AutoReplyRuleVo rule) {
        this.rule = rule;
    }

    public Set<BatchTaskError> getErrors() {
        return errors;
    }

    public void setErrors(Set<BatchTaskError> errors) {
        this.errors = errors;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(Integer filterCount) {
        this.filterCount = filterCount;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
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

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

}
