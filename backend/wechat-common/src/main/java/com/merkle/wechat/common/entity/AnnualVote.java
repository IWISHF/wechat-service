package com.merkle.wechat.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.Base64Utils;

@Entity(name = "annualvote")
public class AnnualVote {
    public static final String VALID = "valid";
    public static final String INIT = "init";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String openid;

    private String nickname;

    private String headimgurl;

    private String voteStr = "0000000000";

    private String voteStatus = INIT;

    private Date created = new Date();

    private Date updated = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getVoteStr() {
        return voteStr;
    }

    public void setVoteStr(String voteStr) {
        this.voteStr = voteStr;
    }

    public String getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(String voteStatus) {
        this.voteStatus = voteStatus;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return new String(Base64Utils.decodeFromString(nickname));
    }
}
