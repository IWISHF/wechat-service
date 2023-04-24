package com.merkle.wechat.modules.demo.service;

import weixin.popular.bean.user.TagsGetidlistResult;

public interface DemoTagService {

    Integer findOrCreateTag(String tagStr, String appid);

    void tagFollower(String openid, Integer tagid, String string);

    void groupFollower(String openid, Integer tagid, String string);

    TagsGetidlistResult getTagList(String string, String openid);

    void tagFollower(String openid, String tagStr);

    void removeTagFromFollower(String openid, String tagStr);

}
