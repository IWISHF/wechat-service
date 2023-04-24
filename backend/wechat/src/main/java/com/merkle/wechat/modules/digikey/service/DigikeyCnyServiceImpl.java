package com.merkle.wechat.modules.digikey.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.merkle.loyalty.response.PrismResponse;
import com.merkle.loyalty.util.JsonUtil;
import com.merkle.wechat.common.dao.digikey.cny.CnyItemDao;
import com.merkle.wechat.common.dao.digikey.cny.CnyItemOpenLogDao;
import com.merkle.wechat.common.dao.digikey.cny.CnyViewToolLogDao;
import com.merkle.wechat.common.dao.digikey.wish.ShareLockDao;
import com.merkle.wechat.common.dao.digikey.wish.ShareLogDao;
import com.merkle.wechat.common.entity.digikey.cny.CnyItem;
import com.merkle.wechat.common.entity.digikey.cny.CnyItemOpenLog;
import com.merkle.wechat.common.entity.digikey.cny.CnyViewToolLog;
import com.merkle.wechat.common.entity.digikey.wish.ShareLock;
import com.merkle.wechat.common.entity.digikey.wish.ShareLog;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.TimeUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.digikey.vo.CnyItemOpenVo;
import com.merkle.wechat.modules.digikey.vo.CnyItemVo;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;

@Component
public class DigikeyCnyServiceImpl {
    protected Logger logger = LoggerFactory.getLogger("DigikeyCnyServiceImpl");
    private String OPENED_WITH_POINTS = "opened_with_points";
    private String OPENED_WITHOUT_POINTS = "opened_without_points";
    private String UNOPENED_EXPIRED = "unopened_expired";
    private String UNOPENED_CURRENT = "unopened_current";
    private String UNOPENED_FUTURE = "unopened_future";
    private @Autowired CnyItemDao cnyItemDaoImpl;
    private @Autowired CnyViewToolLogDao cnyViewTookLogDaoImpl;
    private @Autowired CnyItemOpenLogDao cnyItemOpenLogDaoImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired ShareLockDao shareLockDaoImpl;
    private @Autowired ShareLogDao shareLogDaoImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;

    public List<CnyItemVo> getCnyItemVosByOpenId(String openid) {
        List<CnyItemVo> vos = new ArrayList<>();
        List<CnyItem> items = (List<CnyItem>) cnyItemDaoImpl.findAll();
        Map<Long, CnyItemOpenLog> logsMap = new HashMap<>();
        Set<CnyItemOpenLog> logs = cnyItemOpenLogDaoImpl.findByOpenid(openid);
        logs.forEach((log) -> {
            logsMap.put(log.getCnyItemId(), log);
        });
        items.forEach((item) -> {
            CnyItemOpenLog log = null;
            if (logsMap.containsKey(item.getId())) {
                log = logsMap.get(item.getId());
            }
            vos.add(convertFromCnyItem(log, item));
        });
        return vos;
    }

    public CnyItemVo openCnyItem(CnyItemOpenVo vo) throws Exception {
        Date current = new Date();
        if (current.after(getEndDate())) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        CnyItem item = Optional.ofNullable(cnyItemDaoImpl.findOne(vo.getItemId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (!canOpen(item.getEnableStartDate())) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        CnyItemOpenLog log = new CnyItemOpenLog();
        log.setOpenid(vo.getOpenid());
        log.setOpenDate(current);
        log.setCanOpenDate(item.getEnableStartDate());
        log.setCnyItemId(item.getId());
        boolean canEarnPoints = canEarnPoints(item.getEnableStartDate());
        log.setEarnPoints(canEarnPoints);
        log = cnyItemOpenLogDaoImpl.save(log);
        sendRedPacketOpened(log);
        return convertFromCnyItem(log, item);
    }

    public void viewTool(String openid, String toolId) {
        boolean addPoints = true;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            addPoints = false;
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_cny_t");
            shareLockDaoImpl.save(lock);
            CnyViewToolLog log = new CnyViewToolLog();
            log.setOpenid(openid);
            log.setToolid(toolId);
            log.setStartDateTimeMills(TimeUtil.getTodayStart().getTime());
            log = cnyViewTookLogDaoImpl.save(log);
            int count = cnyViewTookLogDaoImpl.countByOpenidAndToolidAndStartDateTimeMillsAndRecordEventStatus(openid,
                    toolId, log.getStartDateTimeMills(), true);
            if (count < 1 && addPoints) {
                sendCnyViewToolEvent(log);
            }
        } finally {
            shareLockDaoImpl.removeByLock(openid + "_cny_t");
        }
    }

    public void shareToolLimit3(String openid, String content) {
        boolean addPoints = true;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            addPoints = false;
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_cny_s");
            shareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent(content);
            log.setOpenid(openid);
            log.setType("DIGIKEY_CNY");
            int count = shareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndType(openid, content, true,
                    "DIGIKEY_CNY");
            if (count < 3 && addPoints) {
                log.setReachLimit(false);
                log = shareLogDaoImpl.save(log);
                sendShareEvent(log);
            } else {
                log.setReachLimit(true);
                shareLogDaoImpl.save(log);
            }
        } finally {
            shareLockDaoImpl.removeByLock(openid + "_cny_s");
        }
    }

    private CnyItemVo convertFromCnyItem(CnyItemOpenLog log, CnyItem item) {
        CnyItemVo vo = new CnyItemVo();
        vo.setId(item.getId());
        vo.setName(item.getName());
        vo.setOpenedPicUrl(item.getOpenedPicUrl());
        vo.setUnopenCurrentPicUrl(item.getUnopenCurrentPicUrl());
        vo.setUnopenExpiredPicUrl(item.getUnopenExpiredPicUrl());
        vo.setUnopenFuturePicUrl(item.getUnopenFuturePicUrl());
        vo.setEnableStartDate(item.getEnableStartDate());
        vo.setDescription(item.getDescription());
        vo.setPageUrl(item.getPageUrl());
        if (log != null) {
            if (log.isEarnPoints()) {
                vo.setStatus(OPENED_WITH_POINTS);
            } else {
                vo.setStatus(OPENED_WITHOUT_POINTS);
            }
            vo.setOpenDate(log.getOpenDate());
        } else {
            long startOfDayTime = TimeUtil.getTodayStart().getTime();
            long endOfDayTime = TimeUtil.getTodayEnd().getTime();
            long enableStartTime = item.getEnableStartDate().getTime();
            if (enableStartTime < startOfDayTime) {
                vo.setStatus(UNOPENED_EXPIRED);
            } else if (enableStartTime > endOfDayTime) {
                vo.setStatus(UNOPENED_FUTURE);
            } else {
                vo.setStatus(UNOPENED_CURRENT);
            }
        }
        return vo;
    }

    private void sendRedPacketOpened(CnyItemOpenLog log) throws Exception {
        try {
            String value = "0";
            if (log.isEarnPoints()) {
                value = "5";
            }
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId,
                    "cny_redpacket_open", value, log.getCnyItemId().toString());
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            cnyItemOpenLogDaoImpl.save(log);
        }
    }

    private boolean canEarnPoints(Date enableStartDate) {
        long startOfDayTime = TimeUtil.getTodayStart().getTime();
        long endOfDayTime = TimeUtil.getTodayEnd().getTime();
        long enableStartTime = enableStartDate.getTime();
        if (enableStartTime >= startOfDayTime && enableStartTime <= endOfDayTime) {
            return true;
        }
        return false;
    }

    private boolean canOpen(Date enableStartDate) {
        long endOfDayTime = TimeUtil.getTodayEnd().getTime();
        long enableStartTime = enableStartDate.getTime();
        if (enableStartTime <= endOfDayTime) {
            return true;
        }
        return false;
    }

    private void sendCnyViewToolEvent(CnyViewToolLog log) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, "cny_view_tool",
                    log.getToolid());
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            cnyViewTookLogDaoImpl.save(log);
        }
    }

    // 1/22 start
    private Date getStartDate() {
        Calendar c = Calendar.getInstance();
        c.set(2020, 0, 22);
        // c.set(2020, 0, 13);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date getEndDate() {
        Calendar c = Calendar.getInstance();
        c.set(2020, 1, 5);
        // c.set(2020, 0, 26);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private void sendShareEvent(ShareLog log) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, "cny_share_tool");
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

    public boolean watchVideo(String openid) {
        boolean addPoints = true;
        Date current = new Date();
        if (current.after(getEndDate()) || current.before(getStartDate())) {
            addPoints = false;
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_cny_v");
            shareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent("video");
            log.setOpenid(openid);
            log.setType("DIGIKEY_CNY_VIDEO");
            int count = shareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndType(openid, "video", true,
                    "DIGIKEY_CNY_VIDEO");
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
            shareLockDaoImpl.removeByLock(openid + "_cny_v");
        }
        return false;
    }

    private void sendWatchVideoEvent(ShareLog log) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId,
                    "cny_watch_video");
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

}
