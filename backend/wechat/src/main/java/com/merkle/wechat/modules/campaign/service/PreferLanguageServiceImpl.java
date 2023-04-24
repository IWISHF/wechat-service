package com.merkle.wechat.modules.campaign.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.PreferLanguageDao;
import com.merkle.wechat.common.entity.PreferLanguage;
import com.merkle.wechat.common.entity.WechatPublicNo;

@Component
public class PreferLanguageServiceImpl {
    private @Autowired PreferLanguageDao preferLanguageDaoImpl;

    public void saveOrUpdate(PreferLanguage preferLanguage, WechatPublicNo pbNo) {
        PreferLanguage dbLanguage = Optional
                .ofNullable(preferLanguageDaoImpl.findFirstByOpenid(preferLanguage.getOpenid()))
                .orElse(new PreferLanguage());
        dbLanguage.setChannelId(pbNo.getId());
        dbLanguage.setLanguage(preferLanguage.getLanguage());
        dbLanguage.setOpenid(preferLanguage.getOpenid());
        preferLanguageDaoImpl.save(dbLanguage);
    }

    public PreferLanguage getLanguage(WechatPublicNo pbNo, String openid) {
        return preferLanguageDaoImpl.findFirstByOpenidAndChannelId(openid, pbNo.getId());
    }
}
