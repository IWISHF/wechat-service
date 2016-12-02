package com.school.project.framework.wxapi;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

public abstract class WxAbastractAPI implements WxAPI{

	private String uri;
	
	private File fileUpload;
	
	public WxAbastractAPI() {
	 
	}
	
	public WxAbastractAPI(String uri) {
		 this.uri = uri;
	}
	
	public void setFileUpload (File fileUpload) {
		this.fileUpload = fileUpload;
	}

	@Override
	public String sendRquest(int method, int timeout, int retry) {
		String returnVal = null;
		try {
			returnVal = sendRequest(method, timeout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	
	private String sendRequest(int method, int timeout) throws ClientProtocolException, IOException {
		Request request = null;
		if (METHOD_GET == method) {
			request = Request.Get(uri).connectTimeout(timeout);
			return request.execute().returnContent().toString();
		} else {
			request = Request.Post(uri).connectTimeout(timeout);
			
			//upload file.
			if (this.fileUpload != null) {
				FileBody fileBody = new FileBody(fileUpload, ContentType.APPLICATION_OCTET_STREAM);
				HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("media", fileBody).build();
				request.body(reqEntity);
			}
			
			return request.execute().returnContent().toString();
		}
	}
	
}
