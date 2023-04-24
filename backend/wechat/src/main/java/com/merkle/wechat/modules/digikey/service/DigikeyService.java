package com.merkle.wechat.modules.digikey.service;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.digikey.DigikeyLikeOrUnlikeRecord;
import com.merkle.wechat.modules.digikey.vo.LikeOrUnlikeStatisticsVo;

public interface DigikeyService {

    LikeOrUnlikeStatisticsVo saveLikeOrUnlikeRecord(DigikeyLikeOrUnlikeRecord record) throws Exception;

    LikeOrUnlikeStatisticsVo getLikeInfo(String identityId, String pageAlias);

    void tagVipForDigikey(String openid, WechatPublicNo pbNo);

}
