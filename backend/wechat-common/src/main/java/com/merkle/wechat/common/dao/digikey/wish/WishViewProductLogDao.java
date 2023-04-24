package com.merkle.wechat.common.dao.digikey.wish;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.wish.WishViewProductLog;

@Repository
public interface WishViewProductLogDao extends CrudRepository<WishViewProductLog, Long> {

    int countByOpenidAndProductidAndRecordEventStatus(String openid, String productid, boolean b);

    int countByOpenidAndProductidAndStartDateTimeMillsAndRecordEventStatus(String openid, String productid,
            long timeMills, boolean b);

}
