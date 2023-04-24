package com.merkle.wechat.modules.aia.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.merkle.wechat.common.dao.aia.AIAUserInfoDao;
import com.merkle.wechat.common.dao.aia.CaptchaDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.aia.AIAUserInfo;
import com.merkle.wechat.common.entity.aia.Captcha;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.JSONUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.aia.vo.AIAUserInfoVo;
import com.merkle.wechat.modules.aia.vo.GetUserInfoVo;
import com.merkle.wechat.service.follower.FollowerService;

@Component
public class CellMessageServiceImpl implements CellMessageService {
    protected Logger logger = LoggerFactory.getLogger("CellMessageServiceImpl");

    private @Autowired CaptchaDao captchaDaoImpl;
    private @Autowired AIAUserInfoDao aiaUserInfoDaoImpl;
    private @Autowired FollowerService followerServiceImpl;

    private static int EXPIRED_GAP = 5 * 60 * 1000; // ms
    private static String CAPTCHA_CONTENT_FORMAT = "您的绑定验证码为%s，5分钟内有效，如非本人操作，请忽略本短信。";

    @Value("${captcha.branchcode}")
    private String branch_code;
    @Value("${captcha.functioncode}")
    private String function_code;
    @Value("${captcha.path}")
    private String path;

    @Override
    @Transactional
    public void sendCaptcha(String phone, String openid, Long channelId) throws Exception {
        matchCondition(openid, phone);

        Captcha captcha = new Captcha();
        captcha.setCode(generateSixRandomNumber());
        captcha.setPhone(phone);
        captcha.setOpenid(openid);
        captcha.setExpiredTime(new Date(System.currentTimeMillis() + EXPIRED_GAP));
        captcha.setStatus(Captcha.UNUSED);
         
        sendCaptcha(captcha);

        captchaDaoImpl.save(captcha);
    }

    private void matchCondition(String openid, String phone) {
        List<Captcha> oldCaptchas = captchaDaoImpl.findByOpenidOrderByCreatedDateDesc(openid);
        if (oldCaptchas != null) {
            // must less 5 times
            if (oldCaptchas.size() >= 5) {
                throw new ServiceWarn(ExceptionConstants.CAPTCHA_MAX_TIMES_LIMIT);
            }
            if (oldCaptchas.size() > 0) {
                Captcha latestCaptcha = oldCaptchas.get(0);
                boolean sixtySecondsGapLimit = latestCaptcha.getCreatedDate()
                        .after(new Date(System.currentTimeMillis() - 60000));
                // must have 60 seconds gap
                if (sixtySecondsGapLimit) {
                    throw new ServiceWarn(ExceptionConstants.CAPTCHA_SIXTY_SECONDS_LIMIT);
                }
            }
        }

        // must be follower!
        Optional.ofNullable(followerServiceImpl.findOneByOpenid(openid))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));

    }

    private void sendCaptcha(Captcha captcha) throws Exception {
        logger.info("=====start send captcha phone:" + captcha.getPhone() + " code:" + captcha.getCode() + "=====");
        Map<String, String> map = new HashMap<>();
        map.put("BRANCH_CODE", branch_code);
        map.put("FUNCTION_CODE", function_code);
        map.put("DESTINATION_ADDR", captcha.getPhone());
        map.put("CONTENT", String.format(CAPTCHA_CONTENT_FORMAT, captcha.getCode()));
        map.put("POLICY_NUMBER", "");
        map.put("AT_TIME", "");
        String bodyJsonStr = JSONUtil.objectJsonStr(map);
        HttpResponse<JsonNode> response = Unirest.post(path).header("content-type", "application/json")
                .header("accept", "application/json").body(bodyJsonStr).asJson();
        if (response.getStatus() == 200) {
            JSONObject res = response.getBody().getObject();
            String status = res.getString("SMS_MT_STATUS");
            String code = res.getString("SMS_MT_CODE");
            logger.info("=====end send captcha phone" + captcha.getPhone() + " code:" + captcha.getCode()
                    + "response code:" + code + " response status:" + status + "=====");
            if (!code.equals("000")) {
                logger.info("=====end error send captcha phone:" + captcha.getPhone() + " code:" + captcha.getCode()
                        + "=====");
                throw new ServiceWarn(Integer.valueOf("27" + code));
            }
        } else {
            logger.info(
                    "=====end error send captcha phone:" + captcha.getPhone() + " code:" + captcha.getCode() + "=====");
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_ERROR);
        }

    }

    private String generateSixRandomNumber() throws Exception {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Override
    @Transactional
    public void verifyCaptchaAndCreateAIAUserInfo(AIAUserInfoVo vo, WechatPublicNo pbNo) throws Exception {
        Captcha cap = captchaDaoImpl.findLastByPhoneAndCodeOrderByCreatedDateAsc(vo.getPhone(), vo.getCode());
        verifyCaptcha(cap);
        cap.setStatus(Captcha.USED);
        captchaDaoImpl.save(cap);

        AIAUserInfo dbInfo = aiaUserInfoDaoImpl.findByOpenid(vo.getOpenid());
        if (dbInfo != null) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }

        Follower follower = Optional.ofNullable(followerServiceImpl.findOneByOpenid(vo.getOpenid()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        AIAUserInfo info = new AIAUserInfo();
        BeanUtils.copyProperties(vo, info);
        info.setWechatPublicNoId(pbNo.getId());
        info.setUnionid(follower.getUnionid());
        aiaUserInfoDaoImpl.save(info);
    }

    private void verifyCaptcha(Captcha cap) throws Exception {
        if (cap == null) {
            throw new ServiceWarn(ExceptionConstants.CREDENTIAL_NOT_MATCH_ERROR);
        } else if (cap.getStatus().equals(Captcha.USED)) {
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_ALREADY_USED_ERROR);
        } else if (cap.getStatus().equals(Captcha.EXPIRED)) {
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_EXPIRED_ERROR);
        } else if (cap.getExpiredTime().before(new Date())) {
            cap.setStatus(Captcha.EXPIRED);
            captchaDaoImpl.save(cap);
            throw new ServiceWarn(ExceptionConstants.CAPTCHA_EXPIRED_ERROR);
        }
    }

    @Override
    public GetUserInfoVo getAIAUserInfo(String openid, WechatPublicNo pbNo) {
        AIAUserInfo dbinfo = aiaUserInfoDaoImpl.findByOpenidAndWechatPublicNoId(openid, pbNo.getId());
        if (dbinfo != null) {
            GetUserInfoVo vo = new GetUserInfoVo();
            BeanUtils.copyProperties(dbinfo, vo);
            return vo;
        }
        return null;
    }
}
