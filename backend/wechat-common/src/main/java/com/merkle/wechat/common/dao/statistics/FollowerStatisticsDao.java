package com.merkle.wechat.common.dao.statistics;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.statistics.FollowerStatistics;

@Repository
public interface FollowerStatisticsDao extends CrudRepository<FollowerStatistics, Long> {
    @Query(value = "select sum(subscribe), "
            + "dateStr from follower_statistics group by dateStr,appId having dateStr >=:start "
            + "and dateStr <=:end and appId=:appId")
    List<Object[]> statisticsSubscribeByDate(@Param("start") String start, @Param("end") String end,
            @Param("appId") String appId);

    @Query(value = "select sum(unsubscribe), "
            + "dateStr from follower_statistics group by dateStr,appId having dateStr >=:start "
            + "and dateStr <=:end and appId=:appId")
    List<Object[]> statisticsUnSubscribeByDate(@Param("start") String start, @Param("end") String end,
            @Param("appId") String appId);

    @Query(value = "select (sum(subscribe) - sum(unsubscribe)), "
            + "dateStr from follower_statistics group by dateStr,appId having dateStr >=:start "
            + "and dateStr <=:end and appId=:appId")
    List<Object[]> statisticsIncreaseByDate(@Param("start") String start, @Param("end") String end,
            @Param("appId") String appId);

    @Query(value = "select sum(subscribe),sum(unsubscribe),(sum(subscribe) - sum(unsubscribe)), "
            + "dateStr from follower_statistics group by dateStr,appId having dateStr >=:start "
            + "and dateStr <=:end and appId=:appId")
    List<Object[]> statisticsByDate(@Param("start") String start, @Param("end") String end,
            @Param("appId") String appId);
}
