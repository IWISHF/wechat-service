package com.merkle.wechat.weixin;

import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.TagGroup;
import com.merkle.wechat.common.entity.WechatPublicNo;

public interface WeixinUserAdapter {

    Tag syncOrCreateWeixinTag(Tag tag, WechatPublicNo pbNo);


    void syncTagFromWechat(WechatPublicNo pbNo, TagGroup defaultGroup);

}
