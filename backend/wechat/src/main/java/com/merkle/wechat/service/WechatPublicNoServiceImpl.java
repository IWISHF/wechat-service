package com.merkle.wechat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.UserDao;
import com.merkle.wechat.common.dao.WechatPublicNoDao;
import com.merkle.wechat.common.entity.User;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.JSONUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.vo.thridparty.WechatPublicNoVo;

import weixin.popular.api.ComponentAPI;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult.Authorizer_info;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult.Authorizer_info.Business_info;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;

@Component
public class WechatPublicNoServiceImpl implements WechatPublicNoService {
    private @Autowired WechatPublicNoDao wechatPublicNoDaoImpl;

    private @Autowired ThirdPartyService ttpServiceImpl;

    private @Autowired TokenService tokenServiceImpl;

    private @Autowired UserDao userDaoImpl;

    @Override
    public List<WechatPublicNoVo> getAllByAccountId(Long accountId) {
        List<WechatPublicNo> nos = wechatPublicNoDaoImpl.findByAccountIdAndStatus(accountId,
                WechatPublicNo.ALREADY_AUTH);

        return WechatPublicNoVo.convertToWechatPublicNoVo(nos);
    }

    @Override
    public WechatPublicNo findOneByAuthorizerAppid(String pubNoAppId) {
        return wechatPublicNoDaoImpl.findOneByAuthorizerAppid(pubNoAppId);
    }

    @Override
    public WechatPublicNo findOneById(Long channelId) {
        return wechatPublicNoDaoImpl.findOne(channelId);
    }

    @Override
    public WechatPublicNo save(WechatPublicNo pbNo) {
        return wechatPublicNoDaoImpl.save(pbNo);

    }

    @Override
    public void deAuth(String appId) {
        WechatPublicNo pbNo = findOneByAuthorizerAppid(appId);
        if (pbNo != null) {
            pbNo.setStatus(WechatPublicNo.NOT_AUTH);
            pbNo.setUpdatedDate(new Date());
            save(pbNo);
        }
    }

    @Override
    public WechatPublicNo createOrUpdateWechatPbNoByBasicInfo(Authorization_info basicInfo, Long accountId) {
        WechatPublicNo pbNo = findOneByAuthorizerAppid(basicInfo.getAuthorizer_appid());
        if (pbNo == null) {
            pbNo = new WechatPublicNo();
        }
        pbNo.setAuthorizerAppid(basicInfo.getAuthorizer_appid());
        pbNo.setFuncInfo(JSONUtil.objectJsonStr(basicInfo.getFunc_info()));

        ApiGetAuthorizerInfoResult authInfoResult = ComponentAPI.api_get_authorizer_info(
                tokenServiceImpl.getComponentAccessToken(), ttpServiceImpl.getThirdPartyAppId(),
                basicInfo.getAuthorizer_appid());
        Authorizer_info authInfo = authInfoResult.getAuthorizer_info();
        Business_info businessInfo = authInfo.getBusiness_info();
        pbNo.setAlias(authInfo.getAlias());
        pbNo.setHeadImg(authInfo.getHead_img());
        pbNo.setNickName(authInfo.getNick_name());
        pbNo.setOpenCard(businessInfo.getOpen_card());
        pbNo.setOpenPay(businessInfo.getOpen_pay());
        pbNo.setOpenScan(businessInfo.getOpen_scan());
        pbNo.setOpenShake(businessInfo.getOpen_shake());
        pbNo.setOpenStore(businessInfo.getOpen_store());
        pbNo.setPrincipalName(authInfo.getPrincipal_name());
        pbNo.setQrcodeUrl(authInfo.getQrcode_url());
        pbNo.setServiceTypeInfo(authInfo.getService_type_info().getId());
        pbNo.setVerifyTypeInfo(authInfo.getVerify_type_info().getId());
        pbNo.setStatus(WechatPublicNo.ALREADY_AUTH);
        pbNo.setUserName(authInfo.getUser_name());
        pbNo.setAccountId(accountId);
        pbNo = save(pbNo);
        User user = userDaoImpl.findOne(accountId);
        user.getPbNos().add(pbNo);
        userDaoImpl.save(user);
        return pbNo;
    }

    @Override
    public WechatPublicNo findByIdOrThrowNotExistException(Long channelId) throws Exception {
        return Optional.ofNullable(findOneById(channelId))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.WECHAT_PUBLIC_NO_NOT_EXIST));
    }

    @Override
    public List<WechatPublicNoVo> getAllByAccountId(Long accountId, Pageable pageable) {
        List<WechatPublicNo> nos = wechatPublicNoDaoImpl.findByAccountIdAndStatus(accountId,
                WechatPublicNo.ALREADY_AUTH, pageable);

        return WechatPublicNoVo.convertToWechatPublicNoVo(nos);
    }

}
