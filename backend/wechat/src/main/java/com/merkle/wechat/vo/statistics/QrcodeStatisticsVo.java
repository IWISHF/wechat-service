package com.merkle.wechat.vo.statistics;

public class QrcodeStatisticsVo {
    private String qrcodeName;
    private Long qrcodeId;
    private Long yesterdayScanCount;
    private Long yesterdaySubscribeCount;
    private Long totalScan;
    private Long totalSubscribe;

    public String getQrcodeName() {
        return qrcodeName;
    }

    public void setQrcodeName(String qrcodeName) {
        this.qrcodeName = qrcodeName;
    }

    public Long getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(Long qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public Long getYesterdayScanCount() {
        return yesterdayScanCount;
    }

    public void setYesterdayScanCount(Long yesterdayScanCount) {
        this.yesterdayScanCount = yesterdayScanCount;
    }

    public Long getYesterdaySubscribeCount() {
        return yesterdaySubscribeCount;
    }

    public void setYesterdaySubscribeCount(Long yesterdaySubscribeCount) {
        this.yesterdaySubscribeCount = yesterdaySubscribeCount;
    }

    public Long getTotalScan() {
        return totalScan;
    }

    public void setTotalScan(Long totalScan) {
        this.totalScan = totalScan;
    }

    public Long getTotalSubscribe() {
        return totalSubscribe;
    }

    public void setTotalSubscribe(Long totalSubscribe) {
        this.totalSubscribe = totalSubscribe;
    }

}
