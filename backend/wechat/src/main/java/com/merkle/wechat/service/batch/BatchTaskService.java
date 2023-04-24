package com.merkle.wechat.service.batch;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.AutoReplyRule;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.batch.BatchTaskCreateVo;
import com.merkle.wechat.vo.batch.BatchTaskVo;

public interface BatchTaskService {

    void createBatchTask(WechatPublicNo pbNo, BatchTaskCreateVo vo) throws Exception;

    void deleteBatchTask(WechatPublicNo pbNo, Long taskId) throws Exception;

    Pagination<BatchTaskVo> searchByName(WechatPublicNo pbNo, String key, Pageable pageable);

    public String previewBatchTask(WechatPublicNo pbNo, AutoReplyRule rule) throws Exception;

    BatchTaskVo getBatchTask(WechatPublicNo pbNo, Long id);

    void pollingRunBatchTask();

}
