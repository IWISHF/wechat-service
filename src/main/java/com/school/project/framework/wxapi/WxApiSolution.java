package com.school.project.framework.wxapi;

import org.springframework.stereotype.Component;

@Component
public class WxApiSolution {
	
	public String getAccessToken() {
		WxAppAccessTokenAPI api = new WxAppAccessTokenAPI();
		return api.sendRquest(WxAppAccessTokenAPI.METHOD_GET, 1000, 1);
	}

}
