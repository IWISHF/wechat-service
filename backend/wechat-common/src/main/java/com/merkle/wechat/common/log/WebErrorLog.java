package com.merkle.wechat.common.log;

import java.util.List;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.request.HttpRequest;
/**
 * 
 * @author tyao
 *
 */
public class WebErrorLog {
    private String uri;
    private String method;
    private Map<String, List<String>> headers;
    private String payload;
    private int statusCode;
    private String response;

    public WebErrorLog(String uri, String method, Map<String, List<String>> headers, String payload, int statusCode,
            String response) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.payload = payload;
        this.statusCode = statusCode;
        this.response = response;
    }

    public WebErrorLog(HttpRequest request, HttpResponse<JsonNode> response) {
        if (request != null) {
            this.uri = request.getUrl();
            this.method = request.getHttpMethod().name();
            this.headers = request.getHeaders();
        }

        if (response != null) {
            this.statusCode = response.getStatus();
            this.response = response.getBody().toString();
        } else {
            this.statusCode = -1;
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
