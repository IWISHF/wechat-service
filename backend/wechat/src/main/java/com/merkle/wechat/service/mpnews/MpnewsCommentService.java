package com.merkle.wechat.service.mpnews;

import com.merkle.wechat.common.entity.WechatPublicNo;

public interface MpnewsCommentService {

    void syncMpnewsComments(Long mpnewsId, WechatPublicNo pbNo, Long msgDataId);

}
