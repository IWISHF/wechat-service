package com.merkle.wechat.common.entity.jobs;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.merkle.wechat.common.converter.HashMapConverter;
import com.merkle.wechat.common.enums.JobEnum;

@Entity(name = "job_command")
public class JobCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long accountId;
    private Long channelId;
    private Long baseJobId;
    private int status = JobEnum.STATUS_PENDING.getStatus();
    private Long referenceId;
    private String referenceContent;
    private Long secondReferenceId;
    private String secondReferenceContent;
    @Column(columnDefinition = "text")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> params;
    @Lob
    @Column(columnDefinition = "text")
    private String lastError;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
    private Date completedAt;

    public JobCommand() {
        super();
    }

    public JobCommand(BaseJob job, Long referencId, String referenceContent) {
        setAccountId(job.getAccountId());
        setBaseJobId(job.getId());
        setChannelId(job.getChannelId());
        setReferenceId(referencId);
        setReferenceContent(referenceContent);
    }

    public JobCommand(BaseJob job, Map<String, Object> params) {
        setAccountId(job.getAccountId());
        setBaseJobId(job.getId());
        setChannelId(job.getChannelId());
        setParams(params);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceContent() {
        return referenceContent;
    }

    public void setReferenceContent(String referenceContent) {
        this.referenceContent = referenceContent;
    }

    public Long getSecondReferenceId() {
        return secondReferenceId;
    }

    public void setSecondReferenceId(Long secondReferenceId) {
        this.secondReferenceId = secondReferenceId;
    }

    public String getSecondReferenceContent() {
        return secondReferenceContent;
    }

    public void setSecondReferenceContent(String secondReferenceContent) {
        this.secondReferenceContent = secondReferenceContent;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Long getBaseJobId() {
        return baseJobId;
    }

    public void setBaseJobId(Long baseJobId) {
        this.baseJobId = baseJobId;
    }

}
