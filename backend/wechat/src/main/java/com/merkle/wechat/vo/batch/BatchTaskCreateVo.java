package com.merkle.wechat.vo.batch;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.TagGroupCondition;

import io.swagger.annotations.ApiModelProperty;

public class BatchTaskCreateVo {
    @SafeHtml
    private String name;
    @SafeHtml
    private String description;

    // filterConditions
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @ApiModelProperty(notes = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知, 不选为-1")
    private int sex = -1;
    @Valid
    @ApiModelProperty(notes = "创建不必须")
    private Set<TagGroupCondition> groupConditions = new HashSet<>();

    @NotNull
    @ApiModelProperty(notes = "创建必须")
    private AutoReplyRule rule;

    private boolean triggerNow = false;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "yyyy-MM-dd HH:mm:ss")
    private Date triggerDate;

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

    public AutoReplyRule getRule() {
        return rule;
    }

    public void setRule(AutoReplyRule rule) {
        this.rule = rule;
    }

    public boolean isTriggerNow() {
        return triggerNow;
    }

    public void setTriggerNow(boolean triggerNow) {
        this.triggerNow = triggerNow;
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

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

}
