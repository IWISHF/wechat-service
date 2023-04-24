package com.merkle.wechat.modules.digikey.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.loyalty.response.ResponseData;
import com.merkle.wechat.common.dao.campaign.CampaignAnswerDao;
import com.merkle.wechat.common.dao.follower.FollowerBindInfoDao;
import com.merkle.wechat.common.entity.campaign.CampaignAnswer;
import com.merkle.wechat.common.entity.campaign.QuestionAnswer;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.follower.FollowerBindInfoService;

@Component
@Deprecated
public class DigikeyVipDataFixServiceImpl {
    protected Logger logger = LoggerFactory.getLogger("DigikeyVipDataFixServiceImpl");

    private @Autowired CampaignAnswerDao campaignAnswerDaoImpl;
    private @Autowired WechatPublicNoService wechatPublicNoServiceImpl;
    private @Autowired FollowerBindInfoService bindInfoServiceImpl;
    private @Autowired FollowerBindInfoDao followerBindInfoDaoImpl;
    private @Autowired DigikeyService digikeyServiceImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;

    public void fixBindInfo() {
        Iterable<FollowerBindInfo> dbInfos = followerBindInfoDaoImpl.findAll();
        dbInfos.forEach((i) -> {
            try {
                if (i.isSyncToLoyalty() && i.getId() <= 793) {
                    digikeyServiceImpl.tagVipForDigikey(i.getOpenid(),
                            wechatPublicNoServiceImpl.findByIdOrThrowNotExistException(i.getWechatPublicNoId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void fixCampaign() throws Exception {
        fixConcurrent(1L);
        fix(2L);
        fix(3L);
        fix(5L);
        fix(6L);
    }

    private void fix(Long id) {
        logger.info("======= start fix campaign " + id + "============");
        List<CampaignAnswer> answers = campaignAnswerDaoImpl.findByCampaignId(id);
        if (answers != null && answers.size() > 0) {
            int current = 1;
            for (CampaignAnswer answer : answers) {
                logger.info(
                        "======= campaign " + id + " total " + answers.size() + " current " + current + "============");
                try {
                    FollowerBindInfo info = new FollowerBindInfo();
                    info.setOpenid(answer.getOpenid());
                    info.setWechatPublicNoId(answer.getWechatPublicNoId());
                    Set<QuestionAnswer> qas = answer.getQuestions();
                    for (QuestionAnswer qa : qas) {
                        if (qa.getType().equals("profile")) {
                            qa.getOptions().forEach((o) -> {
                                if (o.getTitle().contains("姓名")) {
                                    info.setName(o.getValue());
                                }
                                if (o.getTitle().contains("邮箱")) {
                                    info.setEmail(o.getValue());
                                }
                                if (o.getTitle().contains("手机号码")) {
                                    info.setPhone(o.getValue());
                                }
                                if (o.getTitle().contains("客户编号")) {
                                    info.setDigikeyCustomerNumber(o.getValue());
                                }
                                if (o.getTitle().contains("收货地址") || o.getTitle().contains("地址")) {
                                    info.setAddress(o.getValue());
                                }
                            });
                        }

                        if (qa.getTitle().contains("职业")) {
                            qa.getOptions().forEach((o) -> {
                                if (o != null && o.isSelected()) {
                                    info.setTitle(o.getValue());
                                }
                            });
                        }
                    }
                    info.setFixFrom(id + "");
                    FollowerBindInfo dbBindInfo = Optional
                            .ofNullable(bindInfoServiceImpl.findOneByOpenid(info.getOpenid()))
                            .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
                    info.setWechatPublicNoId(dbBindInfo.getWechatPublicNoId());
                    info.setAppId(dbBindInfo.getAppId());
                    info.setId(dbBindInfo.getId());
                    info.setUpdatedDate(new Date());
                    BeanUtils.copyProperties(info, dbBindInfo);
                    dbBindInfo.setSyncToLoyalty(false);
                    dbBindInfo = followerBindInfoDaoImpl.save(dbBindInfo);
                    ResponseData response = loyaltyServiceImpl.syncBindInfoToLoyalty(dbBindInfo);
                    if (null != response && response.isSuccess()) {
                        dbBindInfo.setSyncToLoyalty(true);
                        followerBindInfoDaoImpl.save(dbBindInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("===== campain error " + " " + " ++++ openid " + answer.getOpenid() + " answerId:"
                            + answer.getCampaignAnswerId() + " campaignId:" + id + "=======");
                } finally {
                    current++;
                }
            }
        }
        logger.info("======= end fix campaign " + id + "============");
    }

    private void fixConcurrent(Long id) throws Exception {
        logger.info("======= start fix campaign " + id + "============");
        List<CampaignAnswer> answers = campaignAnswerDaoImpl.findByCampaignId(id);
        int size = answers.size();
        Integer totalNo = size;
        int syncCount = 0;
        int segmentGap = 1000;
        if (size > segmentGap) {
            int start = 0;
            int end = segmentGap - 1;
            while (syncCount < size) {
                final int totalNofinal = totalNo;
                final int startFinal = start;
                final int endFinal = end;
                AsyncUtil.asyncRun(() -> {
                    fixSeg(startFinal, endFinal, totalNofinal, answers, id);
                });

                syncCount += end - start + 1;
                start = end + 1;
                end += segmentGap;
                if (end > size) {
                    end = size - 1;
                }
            }
        } else {
            fixSeg(0, answers.size() - 1, answers.size(), answers, id);
        }
        logger.info("======= end fix campaign " + id + "============");
    }

    private void fixSeg(int startFinal, int endFinal, int totalNofinal, List<CampaignAnswer> answers, Long id) {
        int current = 1;
        List<CampaignAnswer> seg = answers.subList(startFinal, endFinal + 1);
        for (CampaignAnswer answer : seg) {
            logger.info("======= campaign " + id + " total " + answers.size() + " current " + (current + startFinal)
                    + "============");
            try {
                FollowerBindInfo info = new FollowerBindInfo();
                info.setOpenid(answer.getOpenid());
                info.setWechatPublicNoId(answer.getWechatPublicNoId());
                Set<QuestionAnswer> qas = answer.getQuestions();
                for (QuestionAnswer qa : qas) {
                    if (qa.getType().equals("profile")) {
                        qa.getOptions().forEach((o) -> {
                            if (o.getTitle().contains("姓名")) {
                                info.setName(o.getValue());
                            }
                            if (o.getTitle().contains("邮箱")) {
                                info.setEmail(o.getValue());
                            }
                            if (o.getTitle().contains("手机号码")) {
                                info.setPhone(o.getValue());
                            }
                            if (o.getTitle().contains("客户编号")) {
                                info.setDigikeyCustomerNumber(o.getValue());
                            }
                            if (o.getTitle().contains("收货地址") || o.getTitle().contains("地址")) {
                                info.setAddress(o.getValue());
                            }
                        });
                    }

                    if (qa.getTitle().contains("职业")) {
                        qa.getOptions().forEach((o) -> {
                            if (o != null && o.isSelected()) {
                                info.setTitle(o.getValue());
                            }
                        });
                    }
                }
                info.setFixFrom(id + "");
                FollowerBindInfo dbBindInfo = Optional
                        .ofNullable(bindInfoServiceImpl.findOneByOpenid(info.getOpenid()))
                        .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
                info.setWechatPublicNoId(dbBindInfo.getWechatPublicNoId());
                info.setAppId(dbBindInfo.getAppId());
                info.setId(dbBindInfo.getId());
                info.setUpdatedDate(new Date());
                BeanUtils.copyProperties(info, dbBindInfo);
                dbBindInfo.setSyncToLoyalty(false);
                dbBindInfo = followerBindInfoDaoImpl.save(dbBindInfo);
                ResponseData response = loyaltyServiceImpl.syncBindInfoToLoyalty(dbBindInfo);
                if (null != response && response.isSuccess()) {
                    dbBindInfo.setSyncToLoyalty(true);
                    followerBindInfoDaoImpl.save(dbBindInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("===== campain error " + " " + " ++++ openid " + answer.getOpenid() + " answerId:"
                        + answer.getCampaignAnswerId() + " campaignId:" + id + "=======");
            } finally {
                current++;
            }
        }
    }

}
