package com.merkle.wechat.modules.digikey.service;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.merkle.loyalty.response.PrismResponse;
import com.merkle.loyalty.util.JsonUtil;
import com.merkle.wechat.common.dao.digikey.wish.ShareLockDao;
import com.merkle.wechat.common.dao.digikey.wish.ShareLogDao;
import com.merkle.wechat.common.entity.digikey.wish.ShareLock;
import com.merkle.wechat.common.entity.digikey.wish.ShareLog;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.util.DatesUtil;

@Component
public class WIOServiceImpl {
    protected Logger logger = LoggerFactory.getLogger("DigikeyFrameArduinoServiceImpl");
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired ShareLockDao shareLockDaoImpl;
    private @Autowired ShareLogDao shareLogDaoImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    // Earn pts: 1 times/week
    public boolean shareLimitWeek(String openid, String content, String detail) {
        boolean addPoints = true;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            addPoints = false;
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_wio_s");
            shareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent(content);
            log.setOpenid(openid);
            log.setType("DIGIKEY_WIO_SHARE");
            int count = shareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndTypeAndCreatedDateBetween(openid, content, true,
                    "DIGIKEY_WIO_SHARE", DatesUtil.getCurrentWeekDayStartTime(), DatesUtil.getCurrentWeekDayEndTime());
            if (count < 1 && addPoints) {
                log.setReachLimit(false);
                log = shareLogDaoImpl.save(log);
                sendShareEvent(log, detail);
                return true;
            } else {
                log.setReachLimit(true);
                shareLogDaoImpl.save(log);
            }
        } finally {
            shareLockDaoImpl.removeByLock(openid + "_wio_s");
        }
        return false;
    }

    public boolean watchVideo(String openid, long id, String detail) {
        boolean addPoints = true;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            addPoints = false;
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_wio_v");
            shareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent("video_" + id);
            log.setOpenid(openid);
            log.setType("DIGIKEY_WIO_VIDEO");
            int count = shareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndType(openid, "video_" + id, true,
                    "DIGIKEY_WIO_VIDEO");
            if (count < 1 && addPoints) {
                log.setReachLimit(false);
                log = shareLogDaoImpl.save(log);
                sendWatchVideoEvent(log, detail);
                return true;
            } else {
                log.setReachLimit(true);
                shareLogDaoImpl.save(log);
            }
        } finally {
            shareLockDaoImpl.removeByLock(openid + "_wio_v");
        }
        return false;
    }

    private void sendWatchVideoEvent(ShareLog log, String detail) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId,
                    "wio_watch_video", "", detail);
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            shareLogDaoImpl.save(log);
            int total = shareLogDaoImpl.countByOpenidAndRecordEventStatusAndType(log.getOpenid(), true, "DIGIKEY_WIO_VIDEO");
            if (total == 6) {
                sendQualifyEvent(log);
            }
        }
    }

    private void sendShareEvent(ShareLog log, String detail) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, "wio_share", "", detail);
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            shareLogDaoImpl.save(log);
        }
    }

    private void sendQualifyEvent(ShareLog log) {
        ShareLog qualifyEventLog = new ShareLog();
        qualifyEventLog.setContent("wio_qualify");
        qualifyEventLog.setOpenid(log.getOpenid());
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, "wio_qualify", "", "Wio拥有兑换资格");
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            qualifyEventLog.setRecordEventStatus(isSuccess);
            qualifyEventLog.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            qualifyEventLog.setRecordEventStatus(false);
            qualifyEventLog.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            shareLogDaoImpl.save(qualifyEventLog);
        }
    }

    // 2/5 start
    private Date getStartDate() {
        Calendar c = Calendar.getInstance();
        if (digikeyPubAppId.equals("wx0a1ea68570a894d5")) {
            c.set(2020, 8, 2);
        } else {
            // for test
            c.set(2020, 1, 1);
        }
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date getEndDate() {
        Calendar c = Calendar.getInstance();
        if (digikeyPubAppId.equals("wx0a1ea68570a894d5")) {
            c.set(2020, 9, 13);
        } else {
            // for test
            c.set(2020, 9, 27);
        }
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
