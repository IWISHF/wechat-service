package com.merkle.wechat.vo.statistics;

public class QrcodeStatisticsItem {
    private Long scanCount;
    private Long subscribeCount;
    private Long qrcodeId;
    private String dateStr;

    public QrcodeStatisticsItem() {
    }

    public QrcodeStatisticsItem(Long scanCount, Long subscribeCount, Long qrcodeId, String dateStr) {
        this.scanCount = scanCount;
        this.subscribeCount = subscribeCount;
        this.qrcodeId = qrcodeId;
        this.dateStr = dateStr;
    }

    public Long getScanCount() {
        return scanCount;
    }

    public void setScanCount(Long scanCount) {
        this.scanCount = scanCount;
    }

    public Long getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Long subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public Long getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(Long qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

}
