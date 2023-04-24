package com.merkle.wechat.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.TagGroup;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.Pagination;

public interface TagGroupService {

    List<TagGroup> getAllGroup(Long channelId) throws Exception;

    void createTagGroup(TagGroup group, WechatPublicNo pbNo) throws Exception;

    void deleteTagGroup(Long groupId, WechatPublicNo pbNo);

    void updateTagGroup(TagGroup group, Long groupId, WechatPublicNo pbNo);

    Pagination<TagGroup> getAllGroup(Long channelId, Pageable pageable);

}
