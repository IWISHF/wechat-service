package com.merkle.wechat.common.dao.digikey.wish;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.digikey.wish.ShareLog;

@Repository
public interface ShareLogDao extends CrudRepository<ShareLog, Long> {

    int countByOpenidAndReachLimit(String openid, boolean reachLimit);

    int countByOpenidAndRecordEventStatus(String openid, boolean b);

    int countByOpenidAndContentAndRecordEventStatus(String openid, String content, boolean b);

    int countByOpenidAndContentAndRecordEventStatusAndType(String openid, String content, boolean b, String string);

    int countByOpenidAndRecordEventStatusAndType(String openid, boolean b, String string);

    int countByOpenidAndContentAndRecordEventStatusAndTypeAndCreatedDateBetween(String openid, String content,
            boolean b, String string, Date currentWeekDayStartTime, Date currentWeekDayEndTime);

    @Query(
            value="select count(1) from digikey_wish_share_log where openid = ?1 and recordEventStatus = 1 and type = 'DIGIKEY_FPGA_VIDEO'",
            nativeQuery = true
            )
    int countVideoPlaySuccess(String openid);
}
