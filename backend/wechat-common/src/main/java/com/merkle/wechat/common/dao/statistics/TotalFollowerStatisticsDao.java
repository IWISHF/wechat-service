package com.merkle.wechat.common.dao.statistics;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.statistics.TotalFollowerStatistics;

@Repository
public interface TotalFollowerStatisticsDao extends CrudRepository<TotalFollowerStatistics, Long> {
    @Query(value = "select totalCount, dateStr from follower_statistics_total where dateStr >=:start "
            + "and dateStr <=:end and appId=:appId")
    List<Object[]> findByDateAndAppId(@Param("start") String start, @Param("end") String end,
            @Param("appId") String appId);

    TotalFollowerStatistics findOneByAppIdAndDateStr(String appId, String dateStr);

}
