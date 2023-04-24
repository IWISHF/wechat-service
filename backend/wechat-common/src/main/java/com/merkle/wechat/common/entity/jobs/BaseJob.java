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

@Entity(name = "base_job")
public class BaseJob {
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_FAILED = 3;
    public static final int STATUS_PENDING = 4;
    public static final int STATUS_RETRY = 5;
    public static final int STATUS_PART = 6;
    public static final String TYPE_TAG = "tagSync";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String type;
    private Long useId;
    private String username;
    private Long accountId;
    private Long channelId;
    private Long jobFactoryId;
    private int status = JobEnum.STATUS_PENDING.getStatus();
    private int totalCount;
    private int successCount = 0;
    private int failedCount = 0;
    private int pendingCount = getTotalCount();
    @Column(columnDefinition = "text")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> config;
    @Lob
    @Column(columnDefinition = "text")
    private String lastError;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
    private Date completedAt;

    public BaseJob() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUseId() {
        return useId;
    }

    public void setUseId(Long useId) {
        this.useId = useId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getJobFactoryId() {
        return jobFactoryId;
    }

    public void setJobFactoryId(Long jobFactoryId) {
        this.jobFactoryId = jobFactoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BaseJob [id=" + id + ", name=" + name + ", description=" + description + ", type="
                + type + ", status=" + status + ", totalCount=" + totalCount + ", successCount="
                + successCount + ", failedCount=" + failedCount + ", pendingCount=" + pendingCount
                + "]";
    }
}
