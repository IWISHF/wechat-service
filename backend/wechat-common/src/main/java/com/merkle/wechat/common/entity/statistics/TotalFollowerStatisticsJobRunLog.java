package com.merkle.wechat.common.entity.statistics;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "follower_statistics_total_job_run_log")
@Table(uniqueConstraints = { @UniqueConstraint(name = "pbNoId_dateStr", columnNames = { "pbNoId", "dateStr" }) })
public class TotalFollowerStatisticsJobRunLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long pbNoId;
    @Column(columnDefinition = "varchar(100)")
    private String dateStr;
    private Date createdDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPbNoId() {
        return pbNoId;
    }

    public void setPbNoId(Long pbNoId) {
        this.pbNoId = pbNoId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
