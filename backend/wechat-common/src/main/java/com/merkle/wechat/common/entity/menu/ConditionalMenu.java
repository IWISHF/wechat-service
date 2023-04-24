package com.merkle.wechat.common.entity.menu;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "menu_conditional")
public class ConditionalMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    private String name;

    @Size(max = 3)
    @OrderBy("indexStr asc")
    @JoinColumn(name = "conditional_menu_id")
    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<MenuButton> button;

    @JoinColumn(name = "conditional_menu_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Matchrule matchrule;

    @JsonIgnore
    private String menuid;

    private Long wechatPublicNoId;

    private int indexStr;

    private Date createdDate = new Date();

    @JsonIgnore
    private Date updatedDate = new Date();

    private boolean published = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MenuButton> getButton() {
        return button;
    }

    public void setButton(Set<MenuButton> button) {
        this.button = button;
    }

    public Matchrule getMatchrule() {
        return matchrule;
    }

    public void setMatchrule(Matchrule matchrule) {
        this.matchrule = matchrule;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
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

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
    }

}
