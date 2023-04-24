package com.merkle.wechat.aop;

/**
 * Use this Class to wrap the return data
 * 
 * @author tyao
 *
 */
public class Result {
    private String errorMessage;

    private int errorCode;

    private Object data;

    public Result() {

    }

    public Result(int errorCode, String errorMessage, Object response) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = response;
    }

    public Result(int errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
