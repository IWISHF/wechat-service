package com.merkle.wechat.service;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.vo.BindMiniProgramVo;
import com.merkle.wechat.vo.mini.DecryptDataVo;
import com.merkle.wechat.vo.mini.MiniBindBasicInfoVo;
import com.merkle.wechat.vo.mini.MiniFollowerBasicInfoVo;

public interface MiniProgramService {

    void bind(BindMiniProgramVo vo, Long accountId);

    WechatPublicNo findOneByAuthorizerAppid(String appId);

    Follower code2Session(String code, WechatPublicNo mini);

    String decryptMiniData(String appId, String openid, DecryptDataVo data);

    MiniFollowerBasicInfoVo getBasicInfo(String appId, String openid);

    String bindBasicInfo(String appId, String openid, MiniBindBasicInfoVo vo) throws Exception;

}
