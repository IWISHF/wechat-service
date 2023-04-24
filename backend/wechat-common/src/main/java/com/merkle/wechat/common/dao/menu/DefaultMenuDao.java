package com.merkle.wechat.common.dao.menu;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.menu.DefaultMenu;

@Repository
public interface DefaultMenuDao extends CrudRepository<DefaultMenu, Long> {

    DefaultMenu findByWechatPublicNoId(Long pbNoId);

    DefaultMenu findByWechatPublicNoIdAndPublished(Long channelId, boolean published);

}
