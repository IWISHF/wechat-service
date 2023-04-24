package com.merkle.wechat.vo.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.batch.BatchTaskResultDao;
import com.merkle.wechat.common.entity.batch.BatchTask;
import com.merkle.wechat.common.entity.batch.BatchTaskError;
import com.merkle.wechat.common.entity.batch.BatchTaskResult;
import com.merkle.wechat.vo.autoreply.converter.AutoReplyRuleConverter;

@Component
public class BatchTaskConverter {
    private @Autowired BatchTaskResultDao batchTaskResultDaoImpl;
    private @Autowired AutoReplyRuleConverter ruleConverter;

    public List<BatchTaskVo> convertTasksToVos(List<BatchTask> tasks) {
        List<BatchTaskVo> vos = new ArrayList<BatchTaskVo>();
        if (tasks == null || tasks.size() == 0) {
            return vos;
        }
        tasks.forEach((t) -> {
            vos.add(convertTaskToVo(t));
        });

        return vos;
    }

    public BatchTaskVo convertTaskToVo(BatchTask task) {
        BatchTaskVo vo = new BatchTaskVo();
        BeanUtils.copyProperties(task, vo, "startDate", "endDate", "triggerDate", "rule");
        vo.setRule(ruleConverter.convertAutoReplyRule(task.getRule(), task.getWechatPublicNoId()));
        if (task.getStartDate() != null && task.getEndDate() != null) {
            vo.setStartDate(new Date(task.getStartDate()));
            vo.setEndDate(new Date(task.getEndDate()));
        }

        if (task.getTriggerDate() != null) {
            vo.setTriggerDate(new Date(task.getTriggerDate()));
        }

        List<BatchTaskResult> results = batchTaskResultDaoImpl.findByBatchTaskId(task.getId());
        Integer totalCount = 0;
        Integer filterCount = 0;
        Integer sentCount = 0;
        Integer errorCount = 0;
        Set<BatchTaskError> errors = task.getErrors();
        if (errors != null && errors.size() != 0) {
            for (BatchTaskError error : errors) {
                String retryStatus = error.getRetryStatus();
                switch (retryStatus) {
                    case BatchTaskError.INIT:
                    case BatchTaskError.ERROR:
                    case BatchTaskError.CANT_RETRY_OPENIDS_LESS_2:
                        totalCount += error.getCountOfOpenIds();
                        errorCount += error.getCountOfOpenIds();
                }
            }
            if (results == null || results.size() == 0) {
                vo.setProgressStatus(BatchTask.ERROR);
            }
        }
        if (results != null && results.size() > 0) {
            boolean isFinished = true;
            for (BatchTaskResult result : results) {
                isFinished = isFinished && result.isFinished();
                totalCount += result.getTotalCount();
                filterCount += result.getFilterCount();
                sentCount += result.getSentCount();
                errorCount += result.getErrorCount();
            }
            if (isFinished) {
                vo.setProgressStatus(BatchTask.SUCCESS);
            } else {
                vo.setProgressStatus(BatchTask.SENDING);
            }
        }

        vo.setTotalCount(totalCount);
        vo.setFilterCount(filterCount);
        vo.setSentCount(sentCount);
        vo.setErrorCount(errorCount);
        return vo;
    }
}
