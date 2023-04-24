package com.merkle.wechat.common.dao.jobs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.jobs.BaseJob;

@Repository
public interface JobDao extends PagingAndSortingRepository<BaseJob, Long> {

    Page<BaseJob> findByChannelId(Long channelId, Pageable pageable);

    @Query(value = "select count(1) from base_job where accountId = ?1 and channelId = ?2 and type = ?3 and status in (2, 4)", nativeQuery = true)
    int countInvalidJob(Long accountId, Long channelId, String type);

}
