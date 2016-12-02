package com.school.project.framework.wxapi;

public interface WxAPI {
	public static final int METHOD_GET = 0;
	public static final int METHOD_POST = 1;
	
	String sendRquest(int method, int timeout, int retry) ;
}
