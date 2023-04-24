package com.merkle.wechat.common.dao.jobs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.jobs.JobCommand;

@Repository
public interface JobCommandDao extends PagingAndSortingRepository<JobCommand, Long> {

    Page<JobCommand> findByBaseJobId(Long jobId, Pageable pageable);

}
