package com.merkle.wechat.common.entity.statistics;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "follower_statistics_total")
@Table(indexes = { @Index(name = "dateStr", columnList = "dateStr"),
        @Index(name = "dateHourStr", columnList = "dateHourStr"),
        @Index(name = "wechatPublicNoId", columnList = "wechatPublicNoId"),
        @Index(name = "appId", columnList = "appId") })
public class TotalFollowerStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long wechatPublicNoId;

    @Column(length = 100)
    private String appId;

    private Long totalCount;// total Subscribe count

    @Column(length = 100)
    private String dateStr;// yyyy-MM-dd

    @Column(length = 100)
    private String dateHourStr;// yyyy-MM-dd HH

    private Date createdDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
