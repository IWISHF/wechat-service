package com.merkle.wechat.common.dao.batch;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.batch.BatchTaskScheduleExecutionLog;

@Repository
public interface BatchTaskScheduleExecutionLogDao extends CrudRepository<BatchTaskScheduleExecutionLog, Long> {

}
