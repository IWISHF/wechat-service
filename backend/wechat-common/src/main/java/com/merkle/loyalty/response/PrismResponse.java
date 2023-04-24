package com.merkle.loyalty.response;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PrismResponse {
	private String original_response;
	
	public PrismResponse(String res) {
		this.original_response = res;
	}
	
	@Override
	public String toString() {
		return this.original_response;
	}
	
	public ResponseData toPrismData() {
		ResponseData data = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			data = mapper.readValue(this.original_response, ResponseData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
