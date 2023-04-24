package com.merkle.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.tag.TagVo;

public interface TagService {

    void syncTagFromWechat(WechatPublicNo pbNo);

    void createTag(WechatPublicNo pbNo, Tag tag);

    void tagFollower(WechatPublicNo pbNo, String openid, Integer tagId);

    Tag getTagByName(String name, WechatPublicNo pbNo);

    void updateTag(WechatPublicNo pbNo, Long tagId, Tag tag);

    Pagination<TagVo> searchWeixinTag(String name, Long groupId, WechatPublicNo pbNo, Pageable pageable);

    Tag getTagByWeixinTagId(Integer tagId, Long pbNoId);

    Pagination<TagVo> searchTag(String name, Long groupId, WechatPublicNo pbNo, Pageable pageable);

    List<Tag> getTagByIds(long[] tagIds, WechatPublicNo pbNo);

    void save(Tag tag);

    String deleteTag(WechatPublicNo pbNo, Long id) throws Exception;

}
