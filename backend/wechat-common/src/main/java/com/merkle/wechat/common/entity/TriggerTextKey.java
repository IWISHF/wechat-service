package com.merkle.wechat.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "trigger_text_key")
public class TriggerTextKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "创建时不必须")
    private Long id;

    @ApiModelProperty(notes = "创建时必须")
    @Column(name = "keyStr", columnDefinition = "varchar(300) not null")
    private String key;
    
    @ApiModelProperty(notes = "创建时必须")
    private int indexStr;

    @ApiModelProperty(notes = "创建时必须, 默认完全匹配true", allowableValues = "true,false")
    private boolean completeMatch = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isCompleteMatch() {
        return completeMatch;
    }

    public void setCompleteMatch(boolean completeMatch) {
        this.completeMatch = completeMatch;
    }

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
    }

}
