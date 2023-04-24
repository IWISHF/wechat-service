package com.merkle.wechat.service.jobs;

import java.util.ArrayList;
import java.util.Map;

import com.merkle.wechat.common.entity.jobs.BaseJob;

public interface BaseJobService {
    Map<String, Object> createJob(ArrayList<String> commands, BaseJob job) throws Exception;

    Map<String, Object> executeJob(BaseJob job) throws Exception;
}
