package com.merkle.wechat.modules.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.TagDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.follower.FollowerService;

import weixin.popular.api.UserAPI;
import weixin.popular.bean.user.Tag;
import weixin.popular.bean.user.TagsCreatResult;
import weixin.popular.bean.user.TagsGetResult;
import weixin.popular.bean.user.TagsGetidlistResult;

@Component
public class DemoTagServiceImpl implements DemoTagService {

    protected Logger logger = LoggerFactory.getLogger("TagServiceImpl");

    private @Autowired TokenService tokenServiceImpl;

    private @Autowired FollowerService followerServiceImpl;

    public Integer findOrCreateTag(String tagStr, String appId) {
        TagsGetResult tags = UserAPI.tagsGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId));
        for (Tag tag : tags.getTags()) {
            if (tag.getName().equals(tagStr)) {
                return tag.getId();
            }
        }
        TagsCreatResult result = UserAPI.tagsCreate(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), tagStr);
        return result.getTag().getId();
    }

    public void groupFollower(String openid, Integer tagid, String appId) {
        UserAPI.groupsMembersUpdate(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), openid, tagid + "");
    }

    public void tagFollower(String openid, Integer tagid, String appId) {
        UserAPI.tagsMembersBatchtagging(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), tagid,
                new String[] { openid });
    }

    public TagsGetidlistResult getTagList(String appId, String openId) {
        return UserAPI.tagsGetidlist(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), openId);
    }

    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired TagDao tagDaoImpl;

    @Override
    public void tagFollower(String openid, String tagStr) {
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        Integer tagId = findOrCreateTag(tagStr, follower.getPubNoAppId());
        System.out.println("============addTag tagid: " + tagId);
        tagFollower(openid, tagId, follower.getPubNoAppId());
        String pubNoAppId = follower.getPubNoAppId();
        com.merkle.wechat.common.entity.Tag tag = null;
        if (pubNoAppId.equals("wx50f190ca3a0ee011")) {
            tag = tagDaoImpl.findByNameAndWechatPublicNoId(tagStr, 2L);
        }
        if (pubNoAppId.equals("wx9f5a246a8ff9c3f3")) {
            tag = tagDaoImpl.findByNameAndWechatPublicNoId(tagStr, 3L);
        }

        if (tag != null) {
            boolean match = follower.getTags().stream().anyMatch((t) -> {
                return t.getName().equals(tagStr);
            });
            if (!match) {
                follower.getTags().add(tag);
                followerDaoImpl.save(follower);
            }
        }

        System.out.println("===========" + "add tag: " + tagStr);
    }

    @Override
    public void removeTagFromFollower(String openid, String tagStr) {
        Follower follower = followerServiceImpl.findOneByOpenid(openid);
        Integer tagId = findOrCreateTag(tagStr, follower.getPubNoAppId());
        UserAPI.tagsMembersBatchuntagging(tokenServiceImpl.getPublicNoAccessTokenByAppId(follower.getPubNoAppId()),
                tagId, new String[] { openid });
        System.out.println("===========" + "remove tag: " + tagStr);
    }
}
