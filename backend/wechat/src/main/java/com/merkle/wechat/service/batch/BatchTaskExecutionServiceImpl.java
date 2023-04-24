package com.merkle.wechat.service.batch;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.RefreshFlagDao;
import com.merkle.wechat.common.dao.batch.BatchTaskDao;
import com.merkle.wechat.common.dao.batch.BatchTaskResultDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.RefreshFlag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.batch.BatchTask;
import com.merkle.wechat.common.entity.batch.BatchTaskError;
import com.merkle.wechat.common.entity.batch.BatchTaskResult;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.specification.FollowerCustomerSpecs;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TokenService;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.MessageSendResult;
import weixin.popular.bean.message.massmessage.MassImageMessage;
import weixin.popular.bean.message.massmessage.MassMPnewsMessage;
import weixin.popular.bean.message.massmessage.MassMPvideoMessage;
import weixin.popular.bean.message.massmessage.MassMessage;
import weixin.popular.bean.message.massmessage.MassTextMessage;

@Component
public class BatchTaskExecutionServiceImpl implements BatchTaskExecutionService {
    protected Logger logger = LoggerFactory.getLogger("BatchTaskExecutionServiceImpl");

    private @Autowired RefreshFlagDao refreshFlagDaoImpl;
    private @Autowired BatchTaskDao batchTaskDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;
    private @Autowired TokenService tokenServiceImpl;
    private @Autowired BatchTaskResultDao batchTaskResultDaoImpl;

    @Override
    public void triggerBatchTask(WechatPublicNo pbNo, Long taskId) {
        logger.info("===== start batch Task Id:" + taskId + "======");
        boolean isSuccess = createTaskExecutionFlag(taskId);
        if (!isSuccess) {
            return;
        }
        BatchTask task = batchTaskDaoImpl.findOneByIdAndWechatPublicNoId(taskId, pbNo.getId());

        if (task != null) {
            task = batchSend(task, pbNo);
            task.setAlreadyExecuted(true);
            task.setExecuteDate(new Date());
            batchTaskDaoImpl.save(task);
        }

        removeTaskRefreshFlag(taskId);
        logger.info("===== end batch Task Id:" + taskId + "======");
    }

    @Override
    public int checkBatchTask(BatchTask task, WechatPublicNo pbNo) {
        Specification<Follower> specifications = generateSpecifications(task, pbNo);

        List<Follower> allFollowers = followerDaoImpl.findAll(specifications);
        if (allFollowers == null || allFollowers.size() <= 0) {
            throw new ServiceWarn(ExceptionConstants.BATCH_TASK_CREATE_FOLLOWER_COUNT_ZERO_ERROR);
        } else {
            return allFollowers.size();
        }
    }

    private BatchTask batchSend(BatchTask task, WechatPublicNo pbNo) {
        AutoReplyRule rule = task.getRule();
        if (rule == null) {
            task.setProgressStatus(BatchTask.ERROR);
            return batchTaskDaoImpl.save(task);
        }
        Specification<Follower> specifications = generateSpecifications(task, pbNo);

        Pageable currentPage = new PageRequest(0, 10000);
        Page<Follower> page = followerDaoImpl.findAll(specifications, currentPage);
        int totalPages = page.getTotalPages();

        while (currentPage.getPageNumber() < totalPages && page.getTotalElements() > 0) {
            List<Follower> followers = page.getContent();
            Set<String> openids = followers.parallelStream().map((f) -> {
                return f.getOpenid();
            }).collect(Collectors.toSet());

            if (openids.size() < 2) {
                task.getErrors().add(createBatchTaskErrorForOpenidsLess2(openids));
                task = batchTaskDaoImpl.save(task);
            }

            if (openids.size() >= 2) {
                MassMessage massMessage = generateMessage(rule, openids);
                if (massMessage == null) {
                    task.setProgressStatus(BatchTask.ERROR);
                    return batchTaskDaoImpl.save(task);
                }
                task = submitBatchTaskToWeixin(task, pbNo, massMessage);
            }

            currentPage = new PageRequest(currentPage.getPageNumber() + 1, 10000);
            page = followerDaoImpl.findAll(specifications, currentPage);
        }

        return task;
    }

    private BatchTaskError createBatchTaskErrorForOpenidsLess2(Set<String> toUsers) {
        BatchTaskError error = new BatchTaskError();
        error.setErrorCode("-1");
        error.setErrorMessage("Openids less than 2!");
        error.setOpenIds(toUsers.stream().collect(Collectors.joining(",")));
        error.setRetryStatus(BatchTaskError.CANT_RETRY_OPENIDS_LESS_2);
        error.setTryTimes(0);
        error.setCountOfOpenIds(toUsers.size());
        return error;
    }

    private BatchTask submitBatchTaskToWeixin(BatchTask task, WechatPublicNo pbNo, MassMessage massMessage) {
        MessageSendResult submitResult = MessageAPI.messageMassSend(
                tokenServiceImpl.getPublicNoAccessTokenByAppId(pbNo.getAuthorizerAppid()), massMessage);
        if (submitResult.isSuccess()) {
            BatchTaskResult batchTaskResult = createBatchTaskResult(submitResult, pbNo);
            batchTaskResult.setBatchTaskId(task.getId());
            // 可能涉及多处修改 需要分开保存
            batchTaskResultDaoImpl.save(batchTaskResult);
            return task;
        } else {
            task.getErrors().add(createBatchTaskError(submitResult, massMessage.getTouser()));
            return batchTaskDaoImpl.save(task);
        }

    }

    private BatchTaskError createBatchTaskError(MessageSendResult submitResult, Set<String> toUsers) {
        BatchTaskError error = new BatchTaskError();
        error.setErrorCode(submitResult.getErrcode());
        error.setErrorMessage(submitResult.getErrmsg());
        error.setOpenIds(toUsers.stream().collect(Collectors.joining(",")));
        error.setRetryStatus(BatchTaskError.INIT);
        error.setTryTimes(0);
        return error;
    }

    private BatchTaskResult createBatchTaskResult(MessageSendResult submitResult, WechatPublicNo pbNo) {
        BatchTaskResult result = new BatchTaskResult();
        result.setMsgDataId(submitResult.getMsg_data_id() + "");
        result.setMsgId(submitResult.getMsg_id());
        result.setMsgStatus(submitResult.getMsg_status());
        result.setType(submitResult.getType());
        result.setToUserName(pbNo.getUserName());
        return result;
    }

    private Specification<Follower> generateSpecifications(BatchTask task, WechatPublicNo pbNo) {
        Specifications<Follower> specifications = Specifications
                .where(FollowerCustomerSpecs.pbNoIdIs(pbNo.getAuthorizerAppid()));
        if (task.getSex() != -1) {
            specifications = specifications.and(FollowerCustomerSpecs.sexIs(task.getSex()));
        }

        if (task.getSubscribe() != -1) {
            specifications = specifications.and(FollowerCustomerSpecs.isSubscribe(task.getSubscribe()));
        }

        if (task.getGroupConditions() != null && task.getGroupConditions().size() > 0) {
            specifications = specifications.and(FollowerCustomerSpecs.tagGroupCondition(task.getGroupConditions()));
        }

        if (task.getStartDate() != null && task.getEndDate() != null) {
            specifications = specifications
                    .and(FollowerCustomerSpecs.subscribeTimeBetween(task.getStartDate() / 1000, task.getEndDate() / 1000));
        }
        return specifications;
    }

    public boolean createTaskExecutionFlag(Long taskId) {
        try {
            refreshFlagDaoImpl.save(new RefreshFlag(taskId));
        } catch (Exception e) {
            // 20 minutes ago
            // protect accessToken can be refresh successfully
            Date date = new Date(System.currentTimeMillis() - 1200000);
            RefreshFlag token = refreshFlagDaoImpl.findByTaskIdAndCreatedDateLessThan(taskId, date);
            if (null != token) {
                refreshFlagDaoImpl.delete(token);
            }
            return false;
        }
        return true;
    }

    public void removeTaskRefreshFlag(Long taskId) {
        try {
            refreshFlagDaoImpl.removeByTaskId(taskId);
        } catch (Exception e) {
            logger.info("delete refresh flag exception:" + e.getMessage());
        }
    }

    private MassMessage generateMessage(AutoReplyRule rule, Set<String> toUsers) {
        switch (rule.getReplyType()) {
            case AutoReplyRule.REPLY_TEXT: {
                MassTextMessage message = new MassTextMessage(rule.getReplyTexts());
                message.setTouser(toUsers);
                return message;
            }
            case AutoReplyRule.REPLY_PICTURE: {
                MassImageMessage message = new MassImageMessage(rule.getMediaId());
                message.setTouser(toUsers);
                return message;
            }
            case AutoReplyRule.REPLY_VIDEO: {
                MassMPvideoMessage message = new MassMPvideoMessage(rule.getMediaId());
                message.setTouser(toUsers);
                return message;
            }
            case AutoReplyRule.REPLY_MPNEWS: {
                MassMPnewsMessage message = new MassMPnewsMessage(rule.getMediaId());
                message.setTouser(toUsers);
                return message;
            }
        }
        return null;
    }
}
