package com.school.project.framework.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
@Entity
public class AppAccessToken implements Serializable{
	
	@NotNull
	private Date createTime;
	
	@NotNull
	private Date updateTime = new Date();
	
	@Id
	@NotNull
	@JsonProperty("access_token")
	private String accessToken;
	
	@NotNull
	@JsonProperty("expires_in")
	private long expiresIn;
	
	@OneToOne
	@JoinColumn(name = "app_id")
	private App appId;
	
	public App getAppId() {
		return appId;
	}

	public void setAppId(App appId) {
		this.appId = appId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public boolean isExpiresIn() {
		long now = System.currentTimeMillis();
		long start = getCreateTime().getTime();
		return (now - start) > getExpiresIn() * 1000;
	}
}
