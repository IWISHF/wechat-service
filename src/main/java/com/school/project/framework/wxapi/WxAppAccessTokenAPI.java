package com.school.project.framework.wxapi;

public class WxAppAccessTokenAPI extends WxAbastractAPI {
	private static final String API_URI = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxd3c798b231672956&secret=29e059c279bc7f42a1d5863f59fd1641";
	
	public WxAppAccessTokenAPI() {
		super(API_URI);
	}
	
}
