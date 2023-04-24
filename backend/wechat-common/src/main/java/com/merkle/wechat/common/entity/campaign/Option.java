package com.merkle.wechat.common.entity.campaign;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//TODO: 仓促写完 设计不合理 需要重新设计
@Entity(name = "question_option")
public class Option implements Comparable<Option> {
    public static final String TEXT = "text";
    public static final String CHECKBOX = "checkbox";
    public static final String DATE = "date";
    public static final String EMAIL = "email";
    public static final String NUMBER = "nubmber";
    public static final String PHONE = "tel";
    public static final String RADIO = "radio";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private boolean selected = false;
    private boolean isOther = false;
    private boolean required = false;
    private String type = TEXT;
    private String title;
    private String value;
    @Column(name = "show_order")
    private int order;
    private Date createdDate = new Date();
    private Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Option o) {
        if (this.order < o.getOrder()) {
            return -1;
        } else if (this.order > o.getOrder()) {
            return 1;
        }
        return 0;
    }

}
