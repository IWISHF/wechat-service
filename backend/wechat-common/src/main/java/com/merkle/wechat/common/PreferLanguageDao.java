package com.merkle.wechat.common;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.PreferLanguage;

@Repository
public interface PreferLanguageDao extends CrudRepository<PreferLanguage, Long> {

    PreferLanguage findFirstByOpenid(String openid);

    PreferLanguage findFirstByOpenidAndChannelId(String openid, Long id);

}
