package com.merkle.wechat.service.jobs;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.jobs.BaseJob;
import com.merkle.wechat.common.entity.jobs.JobCommand;
import com.merkle.wechat.common.entity.jobs.JobFactory;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.jobs.JobInvalidVo;

public interface JobManagementService {

    Map<String, Object> createJob(BaseJob job, ArrayList<String> commands, String token) throws Exception;

    String createJobFactory(JobFactory jobFactory) throws Exception;

    Map<String, Object> executeJob(Long jobId) throws Exception;

    Pagination<BaseJob> searchJobs(Long channelId, Long accountId, Pageable pageable) throws Exception;

    Pagination<JobCommand> searchJobCommands(Long jobId, Pageable pageable) throws Exception;

    Pagination<JobFactory> searchJobFactories() throws Exception;

    Map<String, Object> previewJob(BaseJob job, ArrayList<String> commands) throws Exception;

    String invalidJob(JobInvalidVo jobId);

}
