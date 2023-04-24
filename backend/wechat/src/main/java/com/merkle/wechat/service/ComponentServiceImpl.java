package com.merkle.wechat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.service.menu.MenuService;

import weixin.popular.api.ComponentAPI;
import weixin.popular.bean.component.ApiQueryAuthResult;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
import weixin.popular.bean.component.PreAuthCode;

@Component
public class ComponentServiceImpl implements ComponentService {
    protected Logger logger = LoggerFactory.getLogger("ComponentServiceImpl");

    private @Autowired ThirdPartyService ttpServiceImpl;

    private @Autowired TokenService tokenServiceImpl;

    private @Autowired WechatPublicNoService wechatPbNoServiceImpl;

    private @Autowired FollowerService followerServiceImpl;

    private @Autowired JsTicketService jsTicketServiceImpl;

    private @Autowired DefaultAutoReplyService defaultAutoReplyServiceImpl;

    private @Autowired TagService tagServiceImpl;

    private @Autowired MenuService meunServiceImpl;

    public PreAuthCode getPreAuthCode() {
        return ComponentAPI.api_create_preauthcode(tokenServiceImpl.getComponentAccessToken(),
                ttpServiceImpl.getThirdPartyAppId());
    }

    @Override
    public String buildAuthUrl(Long accountId) {
        return ComponentAPI.componentloginpage(ttpServiceImpl.getThirdPartyAppId(), getPreAuthCode().getPre_auth_code(),
                ttpServiceImpl.getBackendDomain() + "/wechat/auth/" + accountId);
    }

    @Override
    public void syncPublicNoInfo(String auth_code, Long accountId) throws Exception {
        logger.info("+++++++++++sync public no start++++++++++++++++++++++++++");
        ApiQueryAuthResult basicResult = ComponentAPI.api_query_auth(tokenServiceImpl.getComponentAccessToken(),
                ttpServiceImpl.getThirdPartyAppId(), auth_code);
        Authorization_info basicInfo = basicResult.getAuthorization_info();

        // Create Token
        tokenServiceImpl.createAccessToken(basicInfo);

        // Create JsTicket
        jsTicketServiceImpl.createOrUpdateJsTicketFromWechat(basicInfo.getAuthorizer_appid());

        // Create pb no
        WechatPublicNo pbNo = wechatPbNoServiceImpl.createOrUpdateWechatPbNoByBasicInfo(basicInfo, accountId);
        String appId = pbNo.getAuthorizerAppid();

        defaultAutoReplyServiceImpl.initDefaultRule(pbNo);
        
        try {
            tagServiceImpl.syncTagFromWechat(pbNo);
        }catch(Exception e) {
            e.printStackTrace();
            logger.info("++++++++++sync tag failed ++++++++++++++++++++++++++");
        }

        AsyncUtil.asyncRun(() -> {
            try {
                meunServiceImpl.syncMenu(pbNo.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            syncFollowers(followerServiceImpl, appId);
        });
        logger.info("+++++++++++sync public no end++++++++++++++++++++++++++");

    }

    private void syncFollowers(FollowerService serviceImpl, String appId) {
        try {
            serviceImpl.syncFollowersFromWechat(appId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("++++++++++sync follower failed ++++++++++++++++++++++++++");
        }
    }

    @Override
    public void deAuth(String appId) {
        wechatPbNoServiceImpl.deAuth(appId);
    }

}
