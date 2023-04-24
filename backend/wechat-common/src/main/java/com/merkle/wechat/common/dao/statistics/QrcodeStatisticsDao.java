package com.merkle.wechat.common.dao.statistics;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.statistics.QrcodeStatistics;

@Repository
public interface QrcodeStatisticsDao extends CrudRepository<QrcodeStatistics, Long> {

    @Query(value = "select count(openid), sum(isSubscribe), "
            + "qrcodeId, scanDate from qrcode_statistics group by scanDate,qrcodeId having scanDate >=:start "
            + "and scanDate <=:end and qrcodeId=:qrcodeId")
    List<Object[]> statisticsByDate(@Param("start") String start, @Param("end") String end,
            @Param("qrcodeId") Long qrcodeId);

    Long countByQrcodeId(Long qrcodeId);

    Long countByQrcodeIdAndIsSubscribe(Long qrcodeId, int isSuscribe);

}
