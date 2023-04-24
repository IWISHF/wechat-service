package com.merkle.wechat.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "user_login_error_log")
public class UserLoginErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String account;

    private Long time;

    private Date created = new Date();

    public Long getId() {
      return this.id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getAccount() {
      return this.account;
    }

    public void setAccount(String account) {
      this.account = account;
    }

    public Long getTime() {
      return this.time;
    }

    public void setTime(Long time) {
      this.time = time;
    }

    public Date getCreated() {
      return this.created;
    }

    public void setCreated(Date created) {
      this.created = created;
    }
}
