package com.merkle.wechat.common.entity.campaign;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "term_answer")
public class TermAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long termAnswerId;
    // CampaignId
    @JsonProperty("id")
    private Long termId;
    private boolean selected;
    private String title;
    @Column(name = "show_order")
    private int order;
    private Date createdDate = new Date();

    public Long getTermAnswerId() {
        return termAnswerId;
    }

    public void setTermAnswerId(Long termAnswerId) {
        this.termAnswerId = termAnswerId;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
