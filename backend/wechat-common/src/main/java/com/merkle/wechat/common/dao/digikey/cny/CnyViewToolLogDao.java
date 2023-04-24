package com.merkle.wechat.common.dao.digikey.cny;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.cny.CnyViewToolLog;

@Repository
public interface CnyViewToolLogDao extends CrudRepository<CnyViewToolLog, Long> {

    int countByOpenidAndToolidAndStartDateTimeMillsAndRecordEventStatus(String openid, String toolId,
            long startDateTimeMills, boolean b);

}
