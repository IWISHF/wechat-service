package com.merkle.wechat.common.dao.statistics;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.statistics.TotalFollowerStatisticsJobRunLog;

@Repository
public interface TotalFollowerStatisticsJobRunLogDao extends CrudRepository<TotalFollowerStatisticsJobRunLog, Long> {

}
