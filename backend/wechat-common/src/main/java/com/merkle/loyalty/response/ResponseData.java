package com.merkle.loyalty.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseData {
	private boolean success;
	private PrismData data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public PrismData getData() {
		return data;
	}

	public void setData(PrismData data) {
		this.data = data;
	}
}
