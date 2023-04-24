package com.merkle.wechat.common.entity.digikey;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "digikey_like_or_unlike_record")
public class DigikeyLikeOrUnlikeRecord {
    public static final String LIKE = "like";
    public static final String UNLIKE = "unlike";
    public static final String NONE = "none";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String identityId;
    private String pageAlias;
    private String vote = NONE;
    private String host;
    private String userAgent;
    private Date created = new Date();
    private Date updated = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getPageAlias() {
        return pageAlias;
    }

    public void setPageAlias(String pageAlias) {
        this.pageAlias = pageAlias;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
