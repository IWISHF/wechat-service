package com.merkle.wechat.common.entity.batch;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "batch_message_task_error")
public class BatchTaskError {
    @Transient
    public static final String INIT = "init";
    @Transient
    public static final String SUCCESS = "success";
    @Transient
    public static final String ERROR = "error";
    @Transient
    public static final String CANT_RETRY_OPENIDS_LESS_2 = "cantRetryBecauseOpenidsLessThan2";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String openIds;

    private String retryStatus = INIT;

    private int tryTimes;

    private int countOfOpenIds;

    private String errorCode;

    private String errorMessage;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenIds() {
        return openIds;
    }

    public void setOpenIds(String openIds) {
        this.openIds = openIds;
    }

    public String getRetryStatus() {
        return retryStatus;
    }

    public void setRetryStatus(String retryStatus) {
        this.retryStatus = retryStatus;
    }

    public int getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public int getCountOfOpenIds() {
        return countOfOpenIds;
    }

    public void setCountOfOpenIds(int countOfOpenIds) {
        this.countOfOpenIds = countOfOpenIds;
    }

}
