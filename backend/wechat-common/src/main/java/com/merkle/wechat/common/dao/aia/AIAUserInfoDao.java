package com.merkle.wechat.common.dao.aia;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.aia.AIAUserInfo;

@Repository
public interface AIAUserInfoDao extends CrudRepository<AIAUserInfo, Long> {

    AIAUserInfo findByOpenidAndUnionid(String openid, String unionid);

    AIAUserInfo findByOpenid(String openid);

    AIAUserInfo findByOpenidAndWechatPublicNoId(String openid, Long id);

}
