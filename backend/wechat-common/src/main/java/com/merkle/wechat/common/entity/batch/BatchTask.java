package com.merkle.wechat.common.entity.batch;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.TagGroupCondition;

@Entity(name = "batch_message_task")
public class BatchTask {
    @Transient
    public static final String SENDING = "sending";
    @Transient
    public static final String ERROR = "error";
    @Transient
    public static final String INIT = "init";
    @Transient
    public static final String SUCCESS = "success";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String appId;
    private String toUserName;

    private String progressStatus = INIT;
    private boolean triggerNow = false;
    private Long triggerDate;
    private boolean alreadyExecuted = false;
    private Date executeDate;

    // filterConditions
    private Long wechatPublicNoId;
    private int subscribe = 1;
    private Long startDate;
    private Long endDate;
    private int sex = -1;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "batch_message_task_tag_group_condition_mapping", joinColumns = @JoinColumn(name = "batchJobId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tagGroupConditionId", referencedColumnName = "id"))
    @OrderBy("id asc")
    private Set<TagGroupCondition> groupConditions = new HashSet<>();

    @JoinColumn(name = "auto_reply_rule_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AutoReplyRule rule;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "batchTaskId")
    private Set<BatchTaskError> errors = new HashSet<>();

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    private boolean enable = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
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

    public Long getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Long triggerDate) {
        this.triggerDate = triggerDate;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
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

    public AutoReplyRule getRule() {
        return rule;
    }

    public void setRule(AutoReplyRule rule) {
        this.rule = rule;
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

    public Set<BatchTaskError> getErrors() {
        return errors;
    }

    public void setErrors(Set<BatchTaskError> errors) {
        this.errors = errors;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isAlreadyExecuted() {
        return alreadyExecuted;
    }

    public void setAlreadyExecuted(boolean alreadyExecuted) {
        this.alreadyExecuted = alreadyExecuted;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

}
