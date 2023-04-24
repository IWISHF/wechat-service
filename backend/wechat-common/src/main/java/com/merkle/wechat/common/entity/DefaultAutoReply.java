package com.merkle.wechat.common.entity;

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
import javax.persistence.OrderBy;

@Entity(name = "default_auto_reply")
public class DefaultAutoReply {
    public static final String FIRST_SUBSCRIBE_REPLY = "firstsubscribeReply";
    public static final String MULTI_SUBSCRIBE_REPLY = "multiSubscribeReply";
    public static final String UN_HIT_KEYWORDS_REPLY = "unHitKeywordsReply";

    public static final String FIRST_SUBSCRIBE_REPLY_FRONT_KEY = "channel.autoreply.default_rule_first_subscribe";
    public static final String MULTI_SUBSCRIBE_REPLY_FRONT_KEY = "channel.autoreply.default_rule_resubscribe";
    public static final String UN_HIT_KEYWORDS_REPLY_FRONT_KEY = "channel.autoreply.default_rule_missing";

    public static final AutoReplyRule DEFAULT_FIRST_REPLY_CONTENT;
    public static final AutoReplyRule DEFAULT_MULTI_REPLY_CONTENT;
    public static final AutoReplyRule DEFAULT_UNHIT_KEYWORDS_REPLY_CONTENT;

    static {
        DEFAULT_FIRST_REPLY_CONTENT = new AutoReplyRule();
        DEFAULT_FIRST_REPLY_CONTENT.setReplyTexts("Hi, 欢迎关注我们的微信公众号。");
        DEFAULT_FIRST_REPLY_CONTENT.setReplyType(AutoReplyRule.REPLY_TEXT);

        DEFAULT_MULTI_REPLY_CONTENT = new AutoReplyRule();
        DEFAULT_MULTI_REPLY_CONTENT.setReplyTexts("Hi, 欢迎回来。");
        DEFAULT_MULTI_REPLY_CONTENT.setReplyType(AutoReplyRule.REPLY_TEXT);

        DEFAULT_UNHIT_KEYWORDS_REPLY_CONTENT = new AutoReplyRule();
        DEFAULT_UNHIT_KEYWORDS_REPLY_CONTENT.setReplyTexts("Hi, 有什么可以帮助您的？");
        DEFAULT_UNHIT_KEYWORDS_REPLY_CONTENT.setReplyType(AutoReplyRule.REPLY_TEXT);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String nameKey;

    private boolean enable;

    private String toUserName;

    private String type;

    private int indexStr;

    private Long wechatPublicNoId;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "defaultAutoReplyId")
    @OrderBy("indexStr asc")
    private Set<AutoReplyRule> autoReplyrules;

    private Date createdDate = new Date();

    private Date updatedDate = new Date();

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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public Set<AutoReplyRule> getAutoReplyrules() {
        return autoReplyrules;
    }

    public void setAutoReplyrules(Set<AutoReplyRule> autoReplyrules) {
        this.autoReplyrules = autoReplyrules;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public int getIndexStr() {
        return indexStr;
    }

    public void setIndexStr(int indexStr) {
        this.indexStr = indexStr;
    }

}
