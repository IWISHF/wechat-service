package com.merkle.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.thridparty.WechatPublicNoVo;

import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;

public interface WechatPublicNoService {

    List<WechatPublicNoVo> getAllByAccountId(Long accountId);

    WechatPublicNo findOneByAuthorizerAppid(String pubNoAppId);

    WechatPublicNo findOneById(Long channelId);

    WechatPublicNo save(WechatPublicNo pbNo);

    void deAuth(String appId);

    WechatPublicNo createOrUpdateWechatPbNoByBasicInfo(Authorization_info basicInfo, Long accountId);

    WechatPublicNo findByIdOrThrowNotExistException(Long channelId) throws Exception;

    List<WechatPublicNoVo> getAllByAccountId(Long accountId, Pageable pageable);

}
