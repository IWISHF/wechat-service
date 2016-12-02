package com.school.project.framework.exceptions;

@SuppressWarnings("serial")
public class SwpException extends Exception {
	public static final int ERROR_CODE = 0;
	public static final String ERROR_MES = "OK";
	
	//errorcode
	public static final int PARSER_JSON_OBJECT_EXCEPTION = 2000;
	public static final int XML_TO_OBJECT_EXCEPTION = 2001;
	
	private int errorcode;
	
	private String errormsg;
	
	private String data;

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
