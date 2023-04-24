package com.merkle.wechat.common.entity.template;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "weixin_notice_template_config")
public class WeixinNoticeTemplateConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // 1 specific value, 2 dynamic value
    @ApiModelProperty(allowableValues = "1,2", value = "1 is specific value, 2 is dynamic value")
    private int type;
    private String field;
    private String fieldValue;
    private int orderIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

}
