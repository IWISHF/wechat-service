package com.merkle.wechat.common.dao.jobs;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.jobs.JobFactory;

@Repository
public interface JobFactoryDao extends PagingAndSortingRepository<JobFactory, Long> {

}
