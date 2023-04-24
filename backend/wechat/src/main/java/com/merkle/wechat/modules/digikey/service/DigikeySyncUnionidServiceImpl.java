package com.merkle.wechat.modules.digikey.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.loyalty.response.ResponseData;
import com.merkle.wechat.common.dao.digikey.DigikeyUnionIdUpdateLogDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.digikey.DigikeyUnionIdUpdateLog;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.util.JSONUtil;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;

@Component
public class DigikeySyncUnionidServiceImpl {
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired DigikeyUnionIdUpdateLogDao logDaoImpl;
    protected Logger logger = LoggerFactory.getLogger("DigikeySyncUnionidServiceImpl");

    public void updateExternalCustomerId(String appId) throws Exception {
        logger.info("=========== customer_id follower all start =========" + appId);
        List<Follower> allUsers = followerDaoImpl.findAllByPubNoAppIdAndRecordToLoyaltySuccess(appId, true);
        logger.info("=========== customer_id follower all end =========" + allUsers.size());

        List<Follower> filtered = new ArrayList<>();
        for (Follower f : allUsers) {
            if (!StringUtils.isEmpty(f.getUnionid())) {
                logger.info("=========== customer_id add ===" + appId + "===" + f.getUnionid());
                filtered.add(f);
            }
        }
        logger.info("=========== customer_id follower filter end =========");
        int total = filtered.size();
        logger.info("=========== customer_id follower total =========" + total);
        int count = 0;
        for (Follower f : filtered) {
            if (f != null && f.isRecordToLoyaltySuccess()) {
                DigikeyUnionIdUpdateLog log = new DigikeyUnionIdUpdateLog();
                log.setAppId(appId);
                log.setOpenid(f.getOpenid());
                log.setUnionid(f.getUnionid());
                try {
                    ResponseData resp = loyaltyServiceImpl.updateExternalCustomerId(f.getOpenid(), appId);
                    if (resp != null && resp.isSuccess()) {
                        log.setSuccess(true);
                    } else {
                        log.setSuccess(false);
                        log.setErrorResp(JSONUtil.objectJsonStr(resp));
                    }
                } catch (Exception e) {
                    log.setErrorResp(e.getMessage());
                    e.printStackTrace();
                } finally {
                    logDaoImpl.save(log);
                    count = count + 1;
                    logger.info("============= end " + count + " of " + total + " =======");
                }
            }
        }
    }

}
