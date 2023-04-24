package com.merkle.wechat.common.dao.batch;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.batch.BatchTask;

@Repository
public interface BatchTaskDao extends PagingAndSortingRepository<BatchTask, Long> {

    BatchTask findOneByIdAndWechatPublicNoId(Long taskId, Long id);

    Page<BatchTask> findByWechatPublicNoIdAndNameContainingAndEnable(Long wechatPublicNoId, String key, boolean enable,
            Pageable pageable);

    List<BatchTask> findByEnableAndAlreadyExecutedAndTriggerDateBetween(boolean enable, boolean isExecuted, Long bottom,
            Long top);

}
