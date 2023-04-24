package com.merkle.wechat.service.jobs;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.UserDao;
import com.merkle.wechat.common.dao.jobs.JobCommandDao;
import com.merkle.wechat.common.dao.jobs.JobDao;
import com.merkle.wechat.common.dao.jobs.JobFactoryDao;
import com.merkle.wechat.common.entity.User;
import com.merkle.wechat.common.entity.jobs.BaseJob;
import com.merkle.wechat.common.entity.jobs.JobCommand;
import com.merkle.wechat.common.entity.jobs.JobFactory;
import com.merkle.wechat.common.enums.JobEnum;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.common.util.AsyncUtil;
import com.merkle.wechat.constant.Constants;
import com.merkle.wechat.security.UserInfo;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.util.JwtUtil;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.jobs.JobInvalidVo;

@Component
public class JobManagementServiceImpl implements JobManagementService {
    private static final String PACKAGE_NAME = JobManagementServiceImpl.class.getPackage().getName();
    protected Logger logger = LoggerFactory.getLogger(JobManagementServiceImpl.class);
    @Autowired
    private JobFactoryDao jobFactoryDaoImpl;
    @Autowired
    private JobDao jobDaoImpl;
    @Autowired
    private JobCommandDao jobCommandDaoImpl;
    @Autowired
    private UserDao userDaoImpl;
    @Autowired
    private TokenService tokenServiceImpl;
    @Autowired
    private WechatPublicNoService pubNoServiceImpl;

    @Override
    @Transactional
    public Map<String, Object> createJob(BaseJob job, ArrayList<String> commands, String token) throws Exception {
        UserInfo userInfo = JwtUtil.parseToken(token);
        User user = userDaoImpl.findOne(Long.parseLong(userInfo.getUserId()));

        job.setUsername(user.getName());
        job.setUseId(user.getId());

        Class<?> target = Class.forName(PACKAGE_NAME + "." + job.getType());
        Method createJob = target.getMethod("createJob", new Class<?>[] {ArrayList.class, BaseJob.class});
        @SuppressWarnings("unchecked")
        Map<String, Object> resMap = (Map<String, Object>) createJob.invoke(
                target
                .getDeclaredConstructor(new Class<?>[] {JobDao.class, JobCommandDao.class})
                .newInstance(jobDaoImpl, jobCommandDaoImpl), commands, job);

        return resMap;
    }

    @Override
    public Pagination<BaseJob> searchJobs(Long channelId, Long accountId, Pageable pageable) throws Exception {
        Page<BaseJob> baseJobPage = jobDaoImpl.findByChannelId(channelId, pageable);
        Pagination<BaseJob> pagination = new Pagination<>();
        List<BaseJob> vos = baseJobPage.getContent();
        BeanUtils.copyProperties(new Pagination<BaseJob>(baseJobPage), pagination, "result");
        pagination.setResult(vos);

        return pagination;
    }

    @Override
    public Map<String, Object> executeJob(Long jobId) throws Exception {
        BaseJob job = jobDaoImpl.findOne(jobId);
        if (job.getStatus() != BaseJob.STATUS_PENDING) {
            throw new ServiceWarn("Can't execute this job because it's stauts not pending", 4005);
        }
        Map<String, Object> res = new HashMap<>();
        AsyncUtil.asyncRun(() -> {
            try {
                Class<?> target = Class.forName(PACKAGE_NAME + "." + job.getType());
                Method executeJob = target.getMethod("executeJob", new Class<?>[] {BaseJob.class});
                executeJob.invoke(target
                        .getDeclaredConstructor(new Class<?>[] {
                            JobDao.class, JobCommandDao.class, TokenService.class, WechatPublicNoService.class})
                        .newInstance(jobDaoImpl, jobCommandDaoImpl, tokenServiceImpl, pubNoServiceImpl), job);
            } catch (Exception e) {
                job.setLastError(e.getMessage());
                job.setStatus(JobEnum.STATUS_FAILED.getStatus());
                job.setCompletedAt(new Date());
                jobDaoImpl.save(job);
                e.printStackTrace();
            }
        });
        res.put("job: ", job.getId());
        return res;
    }

    @Override
    public String createJobFactory(JobFactory jobFactory) throws Exception {
        jobFactoryDaoImpl.save(jobFactory);

        return Constants.RESPONSE_SUCCESS;
    }

    @Override
    public Pagination<JobCommand> searchJobCommands(Long jobId, Pageable pageable) throws Exception {
        Page<JobCommand> jobCommandPage = jobCommandDaoImpl.findByBaseJobId(jobId, pageable);
        Pagination<JobCommand> pagination = new Pagination<>();
        List<JobCommand> vos = jobCommandPage.getContent();
        BeanUtils.copyProperties(new Pagination<JobCommand>(jobCommandPage), pagination, "result");
        pagination.setResult(vos);
        return pagination;
    }

    @Override
    public Pagination<JobFactory> searchJobFactories() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> previewJob(BaseJob job,  ArrayList<String> commands) throws Exception {
        Class<?> target = Class.forName(PACKAGE_NAME + "." + job.getType());
        Method previewJob = target.getMethod("previewJob", new Class<?>[] {BaseJob.class, ArrayList.class});
        return (Map<String, Object>) previewJob.invoke(target
                .getDeclaredConstructor(new Class<?>[] {
                    JobDao.class, JobCommandDao.class, TokenService.class, WechatPublicNoService.class})
                .newInstance(jobDaoImpl, jobCommandDaoImpl, tokenServiceImpl, pubNoServiceImpl), job, commands);
    }


    @Override
    public String invalidJob(JobInvalidVo job) {
        BaseJob baseJob = jobDaoImpl.findOne(job.getJobId());
        if (baseJob != null) {
            baseJob.setStatus(JobEnum.STATUS_INVALID.getStatus());
            baseJob.setLastError(job.getLastError());
            jobDaoImpl.save(baseJob);
            return Constants.RESPONSE_SUCCESS;
        }
        return Constants.RESPONSE_FAILD;
    }
}
