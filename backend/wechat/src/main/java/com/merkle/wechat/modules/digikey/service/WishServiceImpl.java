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
import com.merkle.wechat.common.dao.campaign.CampaignDao;
import com.merkle.wechat.common.dao.digikey.wish.ShareLockDao;
import com.merkle.wechat.common.dao.digikey.wish.ShareLogDao;
import com.merkle.wechat.common.dao.digikey.wish.WishItemDao;
import com.merkle.wechat.common.dao.digikey.wish.WishItemLightLogDao;
import com.merkle.wechat.common.dao.digikey.wish.WishPerfectLogDao;
import com.merkle.wechat.common.dao.digikey.wish.WishViewProductLogDao;
import com.merkle.wechat.common.entity.campaign.Campaign;
import com.merkle.wechat.common.entity.digikey.wish.ShareLock;
import com.merkle.wechat.common.entity.digikey.wish.ShareLog;
import com.merkle.wechat.common.entity.digikey.wish.WishItem;
import com.merkle.wechat.common.entity.digikey.wish.WishItemLightLog;
import com.merkle.wechat.common.entity.digikey.wish.WishPerfectLog;
import com.merkle.wechat.common.entity.digikey.wish.WishViewProductLog;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.JSONUtil;
import com.merkle.wechat.common.util.TimeUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.digikey.vo.LightUpVo;
import com.merkle.wechat.modules.digikey.vo.WishItemVo;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;

@Component
public class WishServiceImpl {
    protected Logger logger = LoggerFactory.getLogger("WishServiceImpl");
    private @Autowired WishItemDao wishItemDaoImpl;
    private @Autowired WishItemLightLogDao wishItemLightDaoImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired WishPerfectLogDao wishPerfectLogDaoImpl;
    private @Autowired ShareLogDao wishShareLogDaoImpl;
    private @Autowired ShareLockDao wishShareLockDaoImpl;
    private @Autowired WishViewProductLogDao wishViewProductLogDaoImpl;
    private @Autowired CampaignDao campaignDaoImpl;
    @Value("${wechat.official.account.appid}")
    private String digikeyPubAppId;
    private static long phase1;
    private static long phase2;
    private static long phase3;
    private static String DIGIKEY_WISH_2020 = "DIGIWISH_2020";

    static {
        Calendar c = Calendar.getInstance();
        c.set(2019, 11, 1);
        // c.set(2019, 10, 19);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        phase1 = c.getTimeInMillis();

        c.set(2019, 11, 9);
        // c.set(2019, 10, 27);
        phase2 = c.getTimeInMillis();

        c.set(2019, 11, 17);
        // c.set(2019, 11, 6);
        phase3 = c.getTimeInMillis();
    }

    public WishItemVo lightUpV2(LightUpVo vo) throws Exception {
        Date current = new Date();
        WishItem item = Optional.ofNullable(wishItemDaoImpl.findOne(vo.getWishItemId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (!canLightUpV2(item.getEnableStartDate())) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        Campaign campaign = campaignDaoImpl.findOne(item.getCampaignId());
        if (campaign == null) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        if (current.after(campaign.getEndDate())) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        WishItemLightLog log = new WishItemLightLog();
        log.setOpenid(vo.getOpenid());
        log.setLightUpDate(current);
        log.setCanLightUpDate(item.getEnableStartDate());
        log.setWishItemId(item.getId());
        log = wishItemLightDaoImpl.save(log);
        if (!earnLightUpPoints(vo.getWishItemId(), vo.getOpenid())) {
            String eventType = this.getLightupEventTypeFromItem(item);
            sendWishLightUp(log, eventType);
        }

        WishItemVo itemVo = new WishItemVo();
        itemVo.setId(item.getId());
        itemVo.setName(item.getName());
        itemVo.setPicUrl(item.getPicUrl());
        itemVo.setEnableStartDate(item.getEnableStartDate());
        itemVo.setDescription(item.getDescription());
        itemVo.setPageUrl(item.getPageUrl());
        itemVo.setPerfect(log.isPerfect());
        itemVo.setAlreadyLightUp(true);
        itemVo.setLightUpDate(log.getLightUpDate());
        return itemVo;
    }

    public WishItemVo lightUp(LightUpVo vo) throws Exception {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.set(2019, 11, 25);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (current.after(c.getTime())) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        WishItem item = Optional.ofNullable(wishItemDaoImpl.findOne(vo.getWishItemId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (!canLightUp(item.getEnableStartDate())) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        WishItemLightLog log = new WishItemLightLog();
        log.setOpenid(vo.getOpenid());
        log.setLightUpDate(current);
        log.setCanLightUpDate(item.getEnableStartDate());
        log.setWishItemId(item.getId());
        boolean canAddPoints = canAddPoints(item.getEnableStartDate());
        log.setPerfect(canAddPoints);
        log = wishItemLightDaoImpl.save(log);
        sendWishLightUp(log);

        WishItemVo itemVo = new WishItemVo();
        itemVo.setId(item.getId());
        itemVo.setName(item.getName());
        itemVo.setPicUrl(item.getPicUrl());
        itemVo.setEnableStartDate(item.getEnableStartDate());
        itemVo.setDescription(item.getDescription());
        itemVo.setPageUrl(item.getPageUrl());
        itemVo.setPerfect(log.isPerfect());
        itemVo.setAlreadyLightUp(true);
        itemVo.setLightUpDate(log.getLightUpDate());
        return itemVo;
    }

    public List<WishItemVo> getWishItemVosByOpenId(String openid) {
        List<WishItemVo> vos = new ArrayList<>();
        List<WishItem> items = (List<WishItem>) wishItemDaoImpl.findAll();
        Map<Long, WishItemLightLog> logsMap = new HashMap<>();
        Set<WishItemLightLog> logs = wishItemLightDaoImpl.findByOpenid(openid);
        logs.forEach((log) -> {
            logsMap.put(log.getWishItemId(), log);
        });
        items.forEach((item) -> {
            WishItemVo vo = new WishItemVo();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setPicUrl(item.getPicUrl());
            vo.setEnableStartDate(item.getEnableStartDate());
            vo.setDescription(item.getDescription());
            vo.setPageUrl(item.getPageUrl());
            if (logsMap.containsKey(item.getId())) {
                WishItemLightLog log = logsMap.get(item.getId());
                vo.setPerfect(log.isPerfect());
                vo.setAlreadyLightUp(true);
                vo.setLightUpDate(log.getLightUpDate());
            }
            vos.add(vo);
        });

        return vos;
    }

    public List<WishItemVo> getWishItemVosByOpenIdAndCampaign(String openid, Long campaignId) {
        List<WishItemVo> vos = new ArrayList<>();
        List<WishItem> items = wishItemDaoImpl.findByCampaignId(campaignId);
        Map<Long, WishItemLightLog> logsMap = new HashMap<>();
        Set<WishItemLightLog> logs = wishItemLightDaoImpl.findByOpenid(openid);
        logs.forEach((log) -> {
            logsMap.put(log.getWishItemId(), log);
        });
        items.forEach((item) -> {
            WishItemVo vo = new WishItemVo();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setPicUrl(item.getPicUrl());
            vo.setEnableStartDate(item.getEnableStartDate());
            vo.setCanSee(false);
            vo.setCanLightUp(false);
            long startOfDayTime = TimeUtil.getTodayStart().getTime();
            long enableStartTime = item.getEnableStartDate().getTime();
            if (enableStartTime <= startOfDayTime) {
                vo.setCanSee(true);
                vo.setCanLightUp(true);
            }
            vo.setDescription(item.getDescription());
            vo.setPageUrl(item.getPageUrl());
            vo.setCustomData(item.getCustomData());
            int count = wishShareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndType(openid,
                    item.getId().toString(), true, DIGIKEY_WISH_2020);
            if (count > 0) {
                vo.setShared(true);
            }
            if (logsMap.containsKey(item.getId())) {
                WishItemLightLog log = logsMap.get(item.getId());
                vo.setPerfect(log.isPerfect());
                vo.setAlreadyLightUp(true);
                vo.setLightUpDate(log.getLightUpDate());
            }
            vos.add(vo);
        });

        return vos;
    }

    public void triggerPerfect() {
        List<String> openids = wishItemLightDaoImpl.findWishPerfectOpenids();
        openids.forEach((openid) -> {
            sendWishPerfectEvent(openid);
        });
    }

    // every product limit 2
    public void shareCampaignLimit2(String openid, String content) {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.set(2019, 11, 25);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (current.after(c.getTime())) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }

        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_s");
            wishShareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent(content);
            log.setOpenid(openid);
            int count = wishShareLogDaoImpl.countByOpenidAndContentAndRecordEventStatus(openid, content, true);
            if (count < 2) {
                log.setReachLimit(false);
                log = wishShareLogDaoImpl.save(log);
                sendWishShareEvent(log);
            } else {
                log.setReachLimit(true);
                wishShareLogDaoImpl.save(log);
            }
        } finally {
            wishShareLockDaoImpl.removeByLock(openid + "_s");
        }
    }

    public void shareCampaignLimit(String openid, String content, int limit) {
        Date current = new Date();

        WishItem item = Optional.ofNullable(wishItemDaoImpl.findOne(Long.valueOf(content)))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (!canLightUpV2(item.getEnableStartDate())) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        Campaign campaign = campaignDaoImpl.findOne(item.getCampaignId());
        if (campaign == null) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        if (current.after(campaign.getEndDate())) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }

        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_s");
            wishShareLockDaoImpl.save(lock);
            ShareLog log = new ShareLog();
            log.setContent(content);
            log.setOpenid(openid);
            log.setType(DIGIKEY_WISH_2020);
            int count = wishShareLogDaoImpl.countByOpenidAndContentAndRecordEventStatusAndType(openid, content, true,
                    DIGIKEY_WISH_2020);
            if (count < limit) {
                log.setReachLimit(false);
                log = wishShareLogDaoImpl.save(log);
                String eventType = this.getShareEventTypeFromItem(item);
                sendWishShareEvent(log, eventType);
            } else {
                log.setReachLimit(true);
                wishShareLogDaoImpl.save(log);
            }
        } finally {
            wishShareLockDaoImpl.removeByLock(openid + "_s");
        }
    }

    public void viewProduct(String openid, String productid) {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.set(2019, 11, 25);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (current.after(c.getTime())) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_p");
            wishShareLockDaoImpl.save(lock);
            WishViewProductLog log = new WishViewProductLog();
            log.setOpenid(openid);
            log.setProductid(productid);
            log.setStartDateTimeMills(TimeUtil.getTodayStart().getTime());
            log = wishViewProductLogDaoImpl.save(log);
            int count = wishViewProductLogDaoImpl.countByOpenidAndProductidAndStartDateTimeMillsAndRecordEventStatus(
                    openid, productid, log.getStartDateTimeMills(), true);
            if (count < 1) {
                sendWishViewProductEvent(log);
            }
        } finally {
            wishShareLockDaoImpl.removeByLock(openid + "_p");
        }
    }

    public void viewProductV2(String openid, String productid) {
        Date current = new Date();

        WishItem item = Optional.ofNullable(wishItemDaoImpl.findOne(Long.valueOf(productid)))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (!canLightUpV2(item.getEnableStartDate())) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        Campaign campaign = campaignDaoImpl.findOne(item.getCampaignId());
        if (campaign == null) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        if (current.after(campaign.getEndDate())) {
            throw new ServiceWarn(ExceptionConstants.CAMPAIGN_CANT_ACCESS_NOW);
        }
        try {
            ShareLock lock = new ShareLock();
            lock.setLock(openid + "_dk2020p");
            wishShareLockDaoImpl.save(lock);
            WishViewProductLog log = new WishViewProductLog();
            log.setOpenid(openid);
            log.setProductid(productid);
            log.setStartDateTimeMills(TimeUtil.getTodayStart().getTime());
            log = wishViewProductLogDaoImpl.save(log);
            int count = wishViewProductLogDaoImpl.countByOpenidAndProductidAndStartDateTimeMillsAndRecordEventStatus(
                    openid, productid, log.getStartDateTimeMills(), true);
            if (count < 1) {
                String evnetTypeString = this.getViewEventTypeFromItem(item);
                sendWishViewProductEvent(log, evnetTypeString);
            }
        } finally {
            wishShareLockDaoImpl.removeByLock(openid + "_dk2020p");
        }
    }

    private void sendWishViewProductEvent(WishViewProductLog log) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId,
                    "wish_view_product", log.getProductid());
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            wishViewProductLogDaoImpl.save(log);
        }
    }

    private void sendWishViewProductEvent(WishViewProductLog log, String eventType) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, eventType,
                    log.getProductid());
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            wishViewProductLogDaoImpl.save(log);
        }
    }

    private void sendWishShareEvent(ShareLog log) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, "wish_share");
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            wishShareLogDaoImpl.save(log);
        }
    }

    private void sendWishShareEvent(ShareLog log, String eventType) {
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, eventType);
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            wishShareLogDaoImpl.save(log);
        }
    }

    private void sendWishPerfectEvent(String openid) {
        WishPerfectLog log = new WishPerfectLog();
        log.setOpenid(openid);
        try {
            PrismResponse response = loyaltyServiceImpl.recordEvent(openid, digikeyPubAppId, "wish_perfect");
            JsonNode jsonNode = JsonUtil.readTree(response.toString());
            boolean isSuccess = jsonNode.get("success").asBoolean();
            log.setRecordEventStatus(isSuccess);
            log.setRecordEventErrorMessage(response.toString());
        } catch (Exception e) {
            log.setRecordEventStatus(false);
            log.setRecordEventErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            wishPerfectLogDaoImpl.save(log);
        }
    }

    private void sendWishLightUp(WishItemLightLog log) throws Exception {
        try {
            String value = "0";
            if (log.isPerfect()) {
                value = "5";
            }
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, "wish_lightup",
                    value, log.getWishItemId().toString());
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
            wishItemLightDaoImpl.save(log);
        }
    }

    private void sendWishLightUp(WishItemLightLog log, String eventType) throws Exception {
        try {
            String value = "0";
            if (log.isPerfect()) {
                value = "5";
            }
            PrismResponse response = loyaltyServiceImpl.recordEvent(log.getOpenid(), digikeyPubAppId, eventType, value,
                    log.getWishItemId().toString());
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
            wishItemLightDaoImpl.save(log);
        }
    }

    private boolean canAddPoints(Date enableStartDate) {
        long enableStartTime = enableStartDate.getTime();
        long startOfDayTime = TimeUtil.getTodayStart().getTime();
        // phase 3
        if (startOfDayTime >= phase3Start()) {
            if (enableStartTime >= phase3Start()) {
                return true;
            }
        } else if (startOfDayTime >= phase2Start()) {// phase 2
            if (enableStartTime < phase3Start() && enableStartTime >= phase2Start()) {
                return true;
            }
        } else if (startOfDayTime >= phase1Start()) {// phase 1
            if (enableStartTime < phase2Start()) {
                return true;
            }
        }
        return false;
    }

    private boolean canLightUp(Date enableStartDate) {
        long enableStartTime = enableStartDate.getTime();
        long startOfDayTime = TimeUtil.getTodayStart().getTime();
        // phase 3
        if (startOfDayTime >= phase3Start()) {
            return true;
        } else if (startOfDayTime >= phase2Start()) {// phase 2

            if (enableStartTime < phase3Start()) {
                return true;
            }
        } else if (startOfDayTime >= phase1Start()) {// phase 1

            if (enableStartTime < phase2Start()) {
                return true;
            }
        }
        return false;
    }

    private boolean canLightUpV2(Date enableStartDate) {
        long enableStartTime = enableStartDate.getTime();
        long startOfDayTime = TimeUtil.getTodayStart().getTime();
        if (enableStartTime <= startOfDayTime) {
            return true;
        }

        return false;
    }

    private String getLightupEventTypeFromItem(WishItem item) {
        @SuppressWarnings("unchecked")
        HashMap<String, String> config = JSONUtil.readValue(item.getCustomData(), HashMap.class);
        String eventTypeString = "xmas_2020_discovery";
        if (config.get("lightupEventType") != null) {
            eventTypeString = config.get("lightupEventType");
        }
        return eventTypeString;
    }

    private String getViewEventTypeFromItem(WishItem item) {
        @SuppressWarnings("unchecked")
        HashMap<String, String> config = JSONUtil.readValue(item.getCustomData(), HashMap.class);
        String eventTypeString = "xmas_2020_view_dk";
        if (config.get("viewEventType") != null) {
            eventTypeString = config.get("viewEventType");
        }
        return eventTypeString;
    }

    private String getShareEventTypeFromItem(WishItem item) {
        @SuppressWarnings("unchecked")
        HashMap<String, String> config = JSONUtil.readValue(item.getCustomData(), HashMap.class);
        String eventTypeString = "xmas_2020_share";
        if (config.get("shareEventType") != null) {
            eventTypeString = config.get("shareEventType");
        }
        return eventTypeString;
    }

    private boolean earnLightUpPoints(Long wishItemId, String openid) {
        int cnt = wishItemLightDaoImpl.countByWishItemIdAndRecordEventStatusAndOpenid(wishItemId, true, openid);
        if (cnt > 0) {
            return true;
        }

        return false;
    }

    public static long phase1Start() {
        return phase1;
    }

    public static long phase2Start() {
        return phase2;
    }

    public static long phase3Start() {
        return phase3;
    }

}
