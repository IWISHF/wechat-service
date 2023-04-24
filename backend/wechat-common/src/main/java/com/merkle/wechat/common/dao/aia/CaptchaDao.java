package com.merkle.wechat.common.dao.aia;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.aia.Captcha;

@Repository
public interface CaptchaDao extends CrudRepository<Captcha, Long> {

    List<Captcha> findByOpenidAndPhone(String openid, String phone);

    List<Captcha> findByOpenidAndPhoneOrderByCreatedDateDesc(String openid, String phone);

    List<Captcha> findByOpenidOrderByCreatedDateDesc(String openid);

    Captcha findLastByPhoneAndCodeOrderByCreatedDateAsc(String phone, String code);

}
