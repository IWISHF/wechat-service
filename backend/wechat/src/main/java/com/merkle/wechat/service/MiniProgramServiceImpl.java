package com.merkle.wechat.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.UserDao;
import com.merkle.wechat.common.dao.WechatPublicNoDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.User;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.vo.BindMiniProgramVo;
import com.merkle.wechat.vo.mini.DecryptDataVo;
import com.merkle.wechat.vo.mini.MiniBindBasicInfoVo;
import com.merkle.wechat.vo.mini.MiniFollowerBasicInfoVo;
import com.merkle.wechat.weixin.Code2SessionResult;
import com.merkle.wechat.weixin.WxCode2SessionAPI;
import com.qq.weixin.mp.aes.WXMiniDataCrypt;

@Component
public class MiniProgramServiceImpl implements MiniProgramService {

    private @Autowired WechatPublicNoDao wechatPublicNoDaoImpl;

    private @Autowired TokenService tokenServiceImpl;

    private @Autowired FollowerDao followerDaoImpl;

    private @Autowired UserDao userDaoImpl;

    private @Autowired LoyaltyService loyaltyServiceImpl;

    @Override
    public void bind(BindMiniProgramVo vo, Long accountId) {
        WechatPublicNo mini = findOneByAuthorizerAppid(vo.getAppId());
        if (mini == null) {
            mini = new WechatPublicNo();
        }
        mini.setAuthorizerAppid(vo.getAppId());

        mini.setNickName(vo.getNickName());
        mini.setStatus(WechatPublicNo.ALREADY_AUTH);
        mini.setAccountId(accountId);
        mini.setMiniAppSecret(vo.getAppSecret());
        mini.setUserName(vo.getUserName());
        mini = save(mini);
        User user = userDaoImpl.findOne(accountId);
        user.getPbNos().add(mini);
        userDaoImpl.save(user);
        tokenServiceImpl.createMiniProgramAccessToken(mini);
    }

    @Override
    public WechatPublicNo findOneByAuthorizerAppid(String pubNoAppId) {
        return wechatPublicNoDaoImpl.findOneByAuthorizerAppid(pubNoAppId);
    }

    public WechatPublicNo save(WechatPublicNo pbNo) {
        return wechatPublicNoDaoImpl.save(pbNo);
    }

    @Override
    public Follower code2Session(String code, WechatPublicNo mini) {
        Code2SessionResult result = WxCode2SessionAPI.code2Session(mini.getAuthorizerAppid(), mini.getMiniAppSecret(),
                code);
        if (!result.isSuccess()) {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
        Follower f = Optional
                .ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(result.getOpenid(), mini.getAuthorizerAppid()))
                .orElseGet(() -> new Follower());
        if (f.isNew()) {
            f.setSubscribeTime((int) (System.currentTimeMillis() / 1000));
            f.setSubscribeScene("MINI_PROGRAM");
            f.setSubscribe(1);
            f.setOpenid(result.getOpenid());
            f.setUnionid(result.getUnionid());
            f.setPubNoAppId(mini.getAuthorizerAppid());
            f.setRecordToLoyaltySuccess(false);
        }
        f.setSessionKey(result.getSession_key());
        f = followerDaoImpl.save(f);
        return f;
    }

    @Override
    public String bindBasicInfo(String appId, String openid, MiniBindBasicInfoVo vo) throws Exception {
        Follower follower = Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, appId))
                .orElseGet(() -> new Follower());
        convertUserToFollower(vo, follower);
        follower.setOpenid(openid);
        follower.setPubNoAppId(appId);
        follower.setRecordToLoyaltySuccess(false);
        follower = followerDaoImpl.save(follower);
        try {
            loyaltyServiceImpl.syncFollowerToLoyalty(openid, appId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    private void convertUserToFollower(MiniBindBasicInfoVo vo, Follower follower) {
        if (vo.getNickname() == null) {
            vo.setNickname("未知");
        }
        follower.setNickname(Base64Utils.encodeToString(vo.getNickname().getBytes()));
        follower.setSubscribeTime((int) (System.currentTimeMillis() / 1000));
        follower.setSubscribeScene("MINI_PROGRAM");
        follower.setSubscribe(1);
        follower.setTagidListStr("{}");
        follower.setCity(vo.getCity());
        follower.setProvince(vo.getProvince());
        follower.setCountry(vo.getCountry());
        follower.setHeadimgurl(vo.getAvatarUrl());
        follower.setLanguage(vo.getLanguage());
        follower.setSex(Integer.valueOf(vo.getGender()));
    }

    @Override
    public String decryptMiniData(String appId, String openid, DecryptDataVo data) {
        Follower f = Optional.ofNullable(followerDaoImpl.findByOpenidAndPubNoAppId(openid, appId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.FOLLOWER_NOT_EXIST));
        String sessionKey = f.getSessionKey();
        if (StringUtils.isEmpty(sessionKey)) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        return WXMiniDataCrypt.decrypt(appId, data.getEncryptData(), sessionKey, data.getIv());
    }

    @Override
    public MiniFollowerBasicInfoVo getBasicInfo(String appId, String openid) {
        WechatPublicNo pbNo = wechatPublicNoDaoImpl.findOneByAuthorizerAppid(appId);
        MiniFollowerBasicInfoVo vo = new MiniFollowerBasicInfoVo();
        vo.setOpenid(openid);
        vo.setWechatPublicNoId(pbNo.getId());
        return vo;
    }
}
