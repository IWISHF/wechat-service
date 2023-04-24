package com.merkle.wechat.service.jobs.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.dao.jobs.JobCommandDao;
import com.merkle.wechat.common.dao.jobs.JobDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.jobs.BaseJob;
import com.merkle.wechat.common.entity.jobs.JobCommand;
import com.merkle.wechat.common.enums.JobEnum;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.jobs.BaseJobService;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;

public class WeChatTemplateMessageJob implements BaseJobService {
    private JobDao jobDaoImpl;
    private JobCommandDao jobCommandDaoImpl;
    private TokenService tokenServiceImpl;
    private WechatPublicNoService pubNoServiceImpl;
    private static int BATCH_SIZE = 5000;

    public WeChatTemplateMessageJob() {
        super();
    }

    public WeChatTemplateMessageJob(JobDao jobDaoImpl, JobCommandDao jobCommandDaoImpl) {
        super();
        this.jobDaoImpl = jobDaoImpl;
        this.jobCommandDaoImpl = jobCommandDaoImpl;
    }

    public WeChatTemplateMessageJob(JobDao jobDaoImpl, JobCommandDao jobCommandDaoImpl,
            TokenService tokenServiceImpl, WechatPublicNoService pubNoServiceImpl) {
        super();
        this.jobDaoImpl = jobDaoImpl;
        this.jobCommandDaoImpl = jobCommandDaoImpl;
        this.tokenServiceImpl = tokenServiceImpl;
        this.pubNoServiceImpl = pubNoServiceImpl;
    }

    @Override
    public Map<String, Object> createJob(ArrayList<String> commands, BaseJob job) throws Exception {
        Map<String, Object> res = new HashMap<>();
        job = jobDaoImpl.save(job);
        String cmdRes = this.createJobCommands(job, commands);
        res.put("job", job.toString());
        res.put("commands", cmdRes);
        return res;
    }

    private String createJobCommands(BaseJob baseJob, ArrayList<String> openids) throws Exception {
        ArrayList<JobCommand> commands = new ArrayList<>();
        openids.forEach((openid) -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("openid", openid);
            JobCommand command = new JobCommand(baseJob, params);
            commands.add(command);
        });
        jobCommandDaoImpl.save(commands);
        baseJob.setTotalCount(commands.size());
        baseJob.setPendingCount(commands.size());
        jobDaoImpl.save(baseJob);
        return "CNT: " + commands.size();
    }

    @Override
    public Map<String, Object> executeJob(BaseJob job) throws Exception {
        Pageable pageable = new PageRequest(0, BATCH_SIZE);
        Page<JobCommand> commandsCommand = jobCommandDaoImpl.findByBaseJobId(job.getId(), pageable);
        commandsCommand.forEach((command) -> {
            sendTemplateMessage(job, command);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("total: ", job.getTotalCount());
        res.put("success: ", job.getSuccessCount());
        res.put("failed: ", job.getFailedCount());
        res.put("pending: ", job.getPendingCount());
        job.setUpdatedAt(new Date());
        job.setCompletedAt(new Date());
        if (job.getTotalCount() == job.getSuccessCount()) {
            job.setStatus(JobEnum.STATUS_SUCCESS.getStatus());
        } else if (job.getTotalCount() == job.getFailedCount()) {
            job.setStatus(JobEnum.STATUS_FAILED.getStatus());
        }
        jobDaoImpl.save(job);
        return res;
    }

    public Map<String, Object> previewJob(BaseJob job, ArrayList<String> commands) {
        WechatPublicNo channel = pubNoServiceImpl.findOneById(job.getChannelId());
        if (channel == null) {
            throw new ServiceWarn(ExceptionConstants.WECHAT_PUBLIC_NO_NOT_EXIST);
        }
        String appId = channel.getAuthorizerAppid();
        Map<String, Object> res = new HashMap<>();

        TemplateMessage message = new TemplateMessage();
        Map<String, Object> config = job.getConfig();
        if (config.get("url") != null && StringUtils.isNotEmpty(config.get("url").toString())) {
            message.setUrl(config.get("url").toString());
        }
        commands.forEach((openid) -> {
            message.setTouser(openid);
            message.setTemplate_id(config.get("templateId").toString());
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            generateTemplateMessageItem(config.get("first").toString(), "#173177", data, "first");
            config.keySet().forEach((key) -> {
                if (!getTemplateCommonItem().contains(key)) {
                    generateTemplateMessageItem(config.get(key).toString(), "#173177", data, key);
                }
            });

            generateTemplateMessageItem(config.get("remark").toString(), "#173177", data, "remark");
            message.setData(data);
            TemplateMessageResult result = MessageAPI
                    .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
            if (result.isSuccess()) {
                res.put(openid, "success");
            } else {
                res.put(openid, result.getErrcode() + "--" + result.getErrmsg());
            }
        });
        return res;
    }

    private void sendTemplateMessage(BaseJob job, JobCommand command) {
        WechatPublicNo channel = pubNoServiceImpl.findOneById(job.getChannelId());
        if (channel == null) {
            throw new ServiceWarn(ExceptionConstants.WECHAT_PUBLIC_NO_NOT_EXIST);
        }
        String appId = channel.getAuthorizerAppid();

        TemplateMessage message = new TemplateMessage();
        Map<String, Object> config = job.getConfig();
        if (config.get("url") != null && StringUtils.isNotEmpty(config.get("url").toString())) {
            message.setUrl(config.get("url").toString());
        }
        Map<String, Object> params = command.getParams();
        message.setTouser(params.get("openid").toString());
        message.setTemplate_id(config.get("templateId").toString());
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
        generateTemplateMessageItem(config.get("first").toString(), "#173177", data, "first");
        config.keySet().forEach((key) -> {
            if (!getTemplateCommonItem().contains(key)) {
                generateTemplateMessageItem(config.get(key).toString(), "#173177", data, key);
            }
        });

        generateTemplateMessageItem(config.get("remark").toString(), "#173177", data, "remark");
        message.setData(data);
        TemplateMessageResult result = MessageAPI
                .messageTemplateSend(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), message);
        if (result.isSuccess()) {
            command.setStatus(JobEnum.STATUS_SUCCESS.getStatus());
            command.setUpdatedAt(new Date());
            jobCommandDaoImpl.save(command);
            job.setSuccessCount(job.getSuccessCount() + 1);
            job.setPendingCount(job.getPendingCount() - 1);
        } else {
            command.setStatus(JobEnum.STATUS_FAILED.getStatus());
            command.setLastError(result.getErrcode() + "--" + result.getErrmsg());
            command.setUpdatedAt(new Date());
            jobCommandDaoImpl.save(command);
            job.setFailedCount(job.getFailedCount() + 1);
            job.setPendingCount(job.getPendingCount() - 1);
        }
    }

    private void generateTemplateMessageItem(String value, String color,
            LinkedHashMap<String, TemplateMessageItem> data, String key) {
        TemplateMessageItem item = new TemplateMessageItem();
        item.setValue(value);
        item.setColor(color);
        data.put(key, item);
    }

    private ArrayList<String> getTemplateCommonItem() {
        ArrayList<String> templateCommonItem = new ArrayList<>();
        templateCommonItem.add("templateId");
        templateCommonItem.add("first");
        templateCommonItem.add("remark");
        templateCommonItem.add("url");

        return templateCommonItem;
    }

}
