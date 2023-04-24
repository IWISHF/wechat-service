package com.merkle.wechat.service.batch;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.batch.BatchTask;

public interface BatchTaskExecutionService {

    void triggerBatchTask(WechatPublicNo pbNo, Long taskId);
    
    int checkBatchTask(BatchTask task, WechatPublicNo pbNo);
}
