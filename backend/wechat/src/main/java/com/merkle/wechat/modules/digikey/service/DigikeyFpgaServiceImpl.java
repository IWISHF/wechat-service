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
public class DigikeyFpgaServiceImpl {
	protected Logger logger = LoggerFactory.getLogger("DigikeyFpgaServiceImpl");
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired ShareLockDao shareLockDaoImpl;
    private @Autowired ShareLogDao shareLogDaoImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    // Earn pts: 1 times/week
    public boolean shareLimitWeek(String openid, String content) {
        boolean addPoints = true;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            addPoints = false;
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_fpga_s");
            shareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent(content);
            log.setOpenid(openid);
            log.setType("DIGIKEY_FPGA_SHARE");
            int count = shareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndTypeAndCreatedDateBetween(openid, content, true,
                    "DIGIKEY_FPGA_SHARE", DatesUtil.getCurrentWeekDayStartTime(), DatesUtil.getCurrentWeekDayEndTime());
            if (count < 1 && addPoints) {
                log.setReachLimit(false);
                log = shareLogDaoImpl.save(log);
                sendShareEvent(log);
                return true;
            } else {
                log.setReachLimit(true);
                shareLogDaoImpl.save(log);
            }
        } finally {
            shareLockDaoImpl.removeByLock(openid + "_fpga_s");
        }
        return false;
    }

    // Earn pts: 1 times/lifetime
    public boolean watchVideo(String openid, long id) {
        boolean addPoints = true;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            addPoints = false;
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_fpga_v");
            shareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent("video_" + id);
            log.setOpenid(openid);
            log.setType("DIGIKEY_FPGA_VIDEO");
            int count = shareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndType(openid, "video_" + id, true,
                    "DIGIKEY_FPGA_VIDEO");
            if (count < 1 && addPoints) {
                log.setReachLimit(false);
                log = shareLogDaoImpl.save(log);
                sendWatchVideoEvent(log);
                return true;
            } else {
                log.setReachLimit(true);
                shareLogDaoImpl.save(log);
            }
        } finally {
            shareLockDaoImpl.removeByLock(openid + "_fpga_v");
        }
        return false;
    }

    public boolean checkLuckyDraw(String openid) {
        boolean luckyDraw = false;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            return luckyDraw;
        }
        int videoCnt = shareLogDaoImpl.countVideoPlaySuccess(openid);
        luckyDraw = videoCnt >= 6;
        return luckyDraw;
    }

    private void sendWatchVideoEvent(ShareLog log) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId,
                    "fpga_watch_video");
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

    private void sendShareEvent(ShareLog log) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, "fpga_share");
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

    private Date getStartDate() {
        Calendar c = Calendar.getInstance();
        if (digikeyPubAppId.equals("wx0a1ea68570a894d5")) {
            c.set(2020, 4, 20);
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
            c.set(2020, 6, 1);
        } else {
            // for test
            c.set(2021, 4, 27);
        }
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
