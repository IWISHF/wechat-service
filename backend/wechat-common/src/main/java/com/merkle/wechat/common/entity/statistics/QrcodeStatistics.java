package com.merkle.wechat.common.entity.statistics;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "qrcode_statistics")
@Table(indexes = { @Index(name = "scanDate", columnList = "scanDate"),
        @Index(name = "scanDateHour", columnList = "scanDateHour"),
        @Index(name = "qrcodeId", columnList = "qrcodeId") })
public class QrcodeStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long qrcodeId;

    private String openid;

    private int isSubscribe;

    @Column(length = 100)
    private String scanDate;// yyyy-MM-dd

    @Column(length = 100)
    private String scanDateHour;// yyyy-MM-dd HH

    private Date createdDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(Long qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getScanDateHour() {
        return scanDateHour;
    }

    public void setScanDateHour(String scanDateHour) {
        this.scanDateHour = scanDateHour;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

}
