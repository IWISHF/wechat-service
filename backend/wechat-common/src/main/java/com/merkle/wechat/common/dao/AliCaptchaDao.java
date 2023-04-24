package com.merkle.wechat.common.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.AliCaptcha;

@Repository
public interface AliCaptchaDao extends CrudRepository<AliCaptcha, Long> {

    List<AliCaptcha> findByOpenidAndPhone(String openid, String phone);

    List<AliCaptcha> findByOpenidAndPhoneOrderByCreatedDateDesc(String openid, String phone);

    List<AliCaptcha> findByOpenidOrderByCreatedDateDesc(String openid);

    AliCaptcha findLastByPhoneAndCodeOrderByCreatedDateAsc(String phone, String code);
}
