package com.merkle.wechat.common.entity.statistics;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "follower_statistics")
@Table(indexes = { @Index(name = "dateStr", columnList = "dateStr"),
        @Index(name = "dateHourStr", columnList = "dateHourStr"), @Index(name = "openid", columnList = "openid"),
        @Index(name = "appId", columnList = "appId") })
public class FollowerStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String openid;

    private int subscribe;

    private int unsubscribe;
    
    @Column(length = 50)
    private String dateStr;// yyyy-MM-dd
    
    @Column(length = 50)
    private String dateHourStr;// yyyy-MM-dd HH

    @Column(length = 100)
    private String appId;

    private Date createdDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public int getUnsubscribe() {
        return unsubscribe;
    }

    public void setUnsubscribe(int unsubscribe) {
        this.unsubscribe = unsubscribe;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getDateHourStr() {
        return dateHourStr;
    }

    public void setDateHourStr(String dateHourStr) {
        this.dateHourStr = dateHourStr;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
