package com.merkle.wechat.weixin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.TagDao;
import com.merkle.wechat.common.entity.TagGroup;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.service.TokenService;

import weixin.popular.api.UserAPI;
import weixin.popular.bean.user.Tag;
import weixin.popular.bean.user.TagsCreatResult;
import weixin.popular.bean.user.TagsGetResult;

@Component
public class WeixinUserAdapterImpl implements WeixinUserAdapter {
    private @Autowired TagDao tagDaoImpl;
    private @Autowired TokenService tokenServiceImpl;

    @Override
    public com.merkle.wechat.common.entity.Tag syncOrCreateWeixinTag(com.merkle.wechat.common.entity.Tag tag,
            WechatPublicNo pbNo) {

        Tag weixinTag = retrieveWeixinTag(tag.getName(), pbNo.getAuthorizerAppid());
        if (weixinTag == null) {
            weixinTag = createWeixinTag(tag.getName(), pbNo.getAuthorizerAppid());
        }

        tag.setCount(Long.valueOf(weixinTag.getCount() == null ? 0 : weixinTag.getCount()));
        tag.setTagId(Long.valueOf(weixinTag.getId()));
        tag.setName(weixinTag.getName());
        tag.setWechatPublicNoId(pbNo.getId());
        return tag;
    }

    private Tag retrieveWeixinTag(String name, String appId) {
        List<Tag> matchTags = retrieveWeixintags(appId).stream().filter((t) -> {
            return t.getName().equals(name);
        }).collect(Collectors.toList());

        if (matchTags != null && !matchTags.isEmpty()) {
            return matchTags.get(0);
        } else {
            return null;
        }
    }

    private List<Tag> retrieveWeixintags(String appId) {
        TagsGetResult result = UserAPI.tagsGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId));
        if (!result.isSuccess()) {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }
        return result.getTags();
    }

    private Tag createWeixinTag(String name, String appId) {
        TagsCreatResult result = UserAPI.tagsCreate(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), name);
        if (!result.isSuccess()) {
            throw new ServiceWarn(result.getErrmsg(), result.getErrcode());
        }

        return result.getTag();
    }

    @Override
    public void syncTagFromWechat(WechatPublicNo pbNo, TagGroup defaultGroup) {

        List<Tag> weixinTags = retrieveWeixintags(pbNo.getAuthorizerAppid());
        List<com.merkle.wechat.common.entity.Tag> dbTags = new ArrayList<>();
        setWeixinTagsToLocalTag(pbNo);

        // TODO: 此处可能出现bug
        weixinTags.forEach((weixinTag) -> {
            com.merkle.wechat.common.entity.Tag dbTag = Optional
                    .ofNullable(tagDaoImpl.findByNameAndWechatPublicNoId(weixinTag.getName(), pbNo.getId()))
                    .orElseGet(() -> new com.merkle.wechat.common.entity.Tag());
            dbTag.setCount(Long.valueOf(weixinTag.getCount()));
            dbTag.setFromWechat(true);
            dbTag.setName(weixinTag.getName());
            dbTag.setUpdatedDate(new Date());
            if (dbTag.getGroup() == null) {
                dbTag.setGroup(defaultGroup);
            }
            dbTag.setWechatPublicNoId(pbNo.getId());
            dbTag.setTagId(Long.valueOf(weixinTag.getId()));
            dbTags.add(dbTag);
        });

        tagDaoImpl.save(dbTags);
    }

    private void setWeixinTagsToLocalTag(WechatPublicNo pbNo) {
        List<com.merkle.wechat.common.entity.Tag> tags = tagDaoImpl.findByWechatPublicNoIdAndFromWechat(pbNo.getId(),
                true);
        tags.forEach((tag) -> {
            tag.setFromWechat(false);
            tag.setUpdatedDate(new Date());
        });
        tagDaoImpl.save(tags);
    }
}
