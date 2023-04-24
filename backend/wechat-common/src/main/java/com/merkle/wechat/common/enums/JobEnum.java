package com.merkle.wechat.common.enums;

public enum JobEnum {
    // Job Status
    STATUS_SUCCESS(1), STATUS_RUNNING(2), STATUS_FAILED(3), STATUS_PENDING(4), STATUS_RETRY(5), STATUS_PART(6), STATUS_INVALID(7);
    private int status;

    JobEnum() {}

    JobEnum(int i) {
        this.setStatus(i);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
