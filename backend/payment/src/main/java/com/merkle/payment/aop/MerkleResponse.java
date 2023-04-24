package com.merkle.payment.aop;

public class MerkleResponse {
    private String errorMessage;

    private int errorCode;

    private Object data;

    public MerkleResponse() {

    }

    public MerkleResponse(int errorCode, String errorMessage, Object response) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = response;
    }

    public MerkleResponse(int errorCode, String errorMessage) {
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
