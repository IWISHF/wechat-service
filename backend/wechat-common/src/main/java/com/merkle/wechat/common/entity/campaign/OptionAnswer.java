package com.merkle.wechat.common.entity.campaign;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "option_answer")
public class OptionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long optionAnswerId;
    // optionId
    @JsonProperty("id")
    private Long optionId;
    private boolean selected;
    private boolean isOther;
    private String title;
    private String value;
    private String type;
    private boolean required;
    @Column(name = "show_order")
    private int order;
    private Date createdDate = new Date();

    public Long getOptionAnswerId() {
        return optionAnswerId;
    }

    public void setOptionAnswerId(Long optionAnswerId) {
        this.optionAnswerId = optionAnswerId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean isOther) {
        this.isOther = isOther;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
