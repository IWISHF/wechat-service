package com.merkle.wechat.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "refresh_flag")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "appId", "taskId" }) })
public class RefreshFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String appId;

    private Long taskId;

    private Date createdDate = new Date();

    public RefreshFlag() {
    }

    public RefreshFlag(String thirdPartyAppId) {
        this.appId = thirdPartyAppId;
    }

    public RefreshFlag(Long taskId) {
        this.taskId = taskId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

}
