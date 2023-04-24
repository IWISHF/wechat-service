package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.KeywordsAutoReply;

@Repository
public interface KeywordsAutoReplyDao extends PagingAndSortingRepository<KeywordsAutoReply, Long> {

    List<KeywordsAutoReply> findByToUserNameAndEnable(String toUserName, boolean enable);

    KeywordsAutoReply findByToUserName(String userName);

    KeywordsAutoReply findOneByIdAndWechatPublicNoId(Long id, Long channelId);

    Page<KeywordsAutoReply> findByNameContainingAndWechatPublicNoId(String name, Long wechatPublicNoId,
            Pageable pageable);

    KeywordsAutoReply findByIdAndWechatPublicNoId(Long keywordsId, Long channelId);

}
