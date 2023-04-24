package com.merkle.wechat.common.dao.batch;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.batch.BatchTaskResult;

@Repository
public interface BatchTaskResultDao extends PagingAndSortingRepository<BatchTaskResult, Long> {

    BatchTaskResult findOneByMsgIdAndToUserName(String msgId, String toUserName);

    List<BatchTaskResult> findByBatchTaskId(Long id);

}
