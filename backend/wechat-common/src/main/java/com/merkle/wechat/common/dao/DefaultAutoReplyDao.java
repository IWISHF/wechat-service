package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.DefaultAutoReply;

@Repository
public interface DefaultAutoReplyDao extends CrudRepository<DefaultAutoReply, Long> {

    DefaultAutoReply findOneByIdAndWechatPublicNoId(Long defaultRuleId, Long pbNoId);

    List<DefaultAutoReply> findByWechatPublicNoId(Long pbNoId);

    DefaultAutoReply findByToUserNameAndEnableAndType(String toUserName, boolean enable, String type);

    List<DefaultAutoReply> findByWechatPublicNoIdOrderByIndexStrAsc(Long channelId);

    DefaultAutoReply findByWechatPublicNoIdAndId(Long channelId, Long defaultAutoReplyId);

}
