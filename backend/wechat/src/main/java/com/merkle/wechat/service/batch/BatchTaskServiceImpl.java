package com.merkle.wechat.service.batch;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.TagDao;
import com.merkle.wechat.common.dao.batch.BatchTaskDao;
import com.merkle.wechat.common.dao.batch.BatchTaskScheduleExecutionLogDao;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.TagGroupCondition;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.batch.BatchTask;
import com.merkle.wechat.common.entity.batch.BatchTaskScheduleExecutionLog;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.common.util.TimeUtil;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.PreviewQrcodeService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.batch.BatchTaskConverter;
import com.merkle.wechat.vo.batch.BatchTaskCreateVo;
import com.merkle.wechat.vo.batch.BatchTaskVo;

@Component
public class BatchTaskServiceImpl implements BatchTaskService {
    protected Logger logger = LoggerFactory.getLogger("BatchTaskServiceImpl");

    private @Autowired BatchTaskDao batchTaskDaoImpl;
    private @Autowired BatchTaskExecutionService executionServiceImpl;
    private @Autowired PreviewQrcodeService previewQrcodeServiceImpl;
    private @Autowired BatchTaskScheduleExecutionLogDao batchTaskScheduleExecutionLogDaoImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired BatchTaskConverter converter;
    private @Autowired TagDao tagDaoImpl;

    @Override
    public String previewBatchTask(WechatPublicNo pbNo, AutoReplyRule rule) throws Exception {
        return previewQrcodeServiceImpl.createPreviewBatchTask(pbNo, rule);
    }

    @Override
    public Pagination<BatchTaskVo> searchByName(WechatPublicNo pbNo, String key, Pageable pageable) {
        Page<BatchTask> page = batchTaskDaoImpl.findByWechatPublicNoIdAndNameContainingAndEnable(pbNo.getId(), key,
                true, pageable);
        List<BatchTask> tasks = page.getContent();
        Pagination<BatchTaskVo> pagination = new Pagination<>();
        List<BatchTaskVo> vos = converter.convertTasksToVos(tasks);
        BeanUtils.copyProperties(new Pagination<BatchTask>(page), pagination, "result");
        pagination.setResult(vos);
        return pagination;
    }

    @Override
    public BatchTaskVo getBatchTask(WechatPublicNo pbNo, Long taskId) {
        BatchTask task = Optional.ofNullable(batchTaskDaoImpl.findOneByIdAndWechatPublicNoId(taskId, pbNo.getId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        return converter.convertTaskToVo(task);
    }

    @Override
    public void deleteBatchTask(WechatPublicNo pbNo, Long taskId) throws Exception {
        BatchTask task = Optional.ofNullable(batchTaskDaoImpl.findOneByIdAndWechatPublicNoId(taskId, pbNo.getId()))
                .orElseThrow(() -> new ServiceWarn(ExceptionConstants.NOT_EXIST));
        if (task.getProgressStatus().equalsIgnoreCase(BatchTask.SENDING)) {
            throw new ServiceWarn(ExceptionConstants.BATCH_TASK_CANT_DELETE);
        }
        task.setEnable(false);
        task.setUpdatedDate(new Date());
        batchTaskDaoImpl.save(task);
    }

    @Override
    public void createBatchTask(WechatPublicNo pbNo, BatchTaskCreateVo vo) throws Exception {
        BatchTask task = copyFromCreateVo(pbNo, vo);
        @SuppressWarnings("unused")
        int followerInvolveCount = executionServiceImpl.checkBatchTask(task, pbNo);
        task = batchTaskDaoImpl.save(task);
        Long taskId = task.getId();

        if (task.isTriggerNow()) {
            AsyncUtil.asyncRun(() -> {
                executionServiceImpl.triggerBatchTask(pbNo, taskId);
            });
        }
    }

    @Override
    public void pollingRunBatchTask() {
        logger.info("===== start polling execute batch task =====");
        long currentTimeMillis = TimeUtil.currentTimeMillis();
        // 前后五分钟内
        Long bottom = currentTimeMillis - 3000000;
        Long top = currentTimeMillis + 3000000;
        List<BatchTask> tasksNeedToExecute = batchTaskDaoImpl.findByEnableAndAlreadyExecutedAndTriggerDateBetween(true,
                false, bottom, top);
        if (tasksNeedToExecute == null || tasksNeedToExecute.size() == 0) {
            return;
        }

        for (BatchTask task : tasksNeedToExecute) {
            try {
                BatchTaskScheduleExecutionLog log = new BatchTaskScheduleExecutionLog();
                log.setTaskId(task.getId());
                batchTaskScheduleExecutionLogDaoImpl.save(log);
            } catch (Exception e) {
                logger.info("===== already run, skip batch task " + task.getName() + " =====");
                continue;
            }
            try {
                WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(task.getWechatPublicNoId());
                if (pbNo.getStatus().equals(WechatPublicNo.ALREADY_AUTH)) {
                    AsyncUtil.asyncRun(() -> {
                        executionServiceImpl.triggerBatchTask(pbNo, task.getId());
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        logger.info("===== End polling execute batch task =====");
    }

    private BatchTask copyFromCreateVo(WechatPublicNo pbNo, BatchTaskCreateVo vo) {
        BatchTask task = new BatchTask();
        BeanUtils.copyProperties(vo, task, "triggerDate", "startDate", "endDate", "groupConditions");
        if (vo.getTriggerDate() != null) {
            task.setTriggerDate(vo.getTriggerDate().getTime());
        }

        task.setWechatPublicNoId(pbNo.getId());
        task.setAppId(pbNo.getAuthorizerAppid());
        task.setToUserName(pbNo.getUserName());

        if (vo.getStartDate() != null && vo.getEndDate() != null) {
            if (vo.getStartDate().after(vo.getEndDate())) {
                throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
            }
            task.setStartDate(vo.getStartDate().getTime());
            task.setEndDate(vo.getEndDate().getTime());
        }
        if (vo.getGroupConditions() != null) {
            vo.getGroupConditions();
            for (TagGroupCondition c : vo.getGroupConditions()) {
                long[] tagIds = c.getTags().stream().mapToLong((t) -> {
                    return t.getId();
                }).toArray();

                if (tagIds.length > 0) {
                    c.getTags().clear();
                    c.getTags().addAll(tagDaoImpl.findByIdInAndWechatPublicNoId(tagIds, pbNo.getId()));
                }
            }
            task.getGroupConditions().addAll(vo.getGroupConditions());
        }

        return task;
    }

}
