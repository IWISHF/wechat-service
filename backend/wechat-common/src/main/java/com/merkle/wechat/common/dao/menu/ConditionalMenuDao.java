package com.merkle.wechat.common.dao.menu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.menu.ConditionalMenu;

@Repository
public interface ConditionalMenuDao extends CrudRepository<ConditionalMenu, Long> {

    List<ConditionalMenu> findByWechatPublicNoIdAndPublished(Long channelId, boolean isPublished);

    ConditionalMenu findOneByWechatPublicNoIdAndIdAndPublished(Long channelId, Long id, boolean isPublished);

    List<ConditionalMenu> findByWechatPublicNoIdAndPublishedOrderByCreatedDateDesc(Long channelId, boolean isPublished);

    List<ConditionalMenu> findByWechatPublicNoIdAndPublishedOrderByIndexStrAsc(Long channelId, boolean isPublished);

    int countByWechatPublicNoId(Long pbNoId);

    List<ConditionalMenu> findByWechatPublicNoIdAndPublishedOrderByIndexStrDesc(Long channelId, boolean b);

    ConditionalMenu findByWechatPublicNoIdAndPublishedAndId(Long channelId, boolean published, Long id);

    List<ConditionalMenu> findByMatchrule_tag_id(Long id);

}
