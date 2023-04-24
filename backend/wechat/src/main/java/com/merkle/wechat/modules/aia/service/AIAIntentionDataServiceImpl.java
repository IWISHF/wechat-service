package com.merkle.wechat.modules.aia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.aia.AIAIntentionDataDao;
import com.merkle.wechat.common.entity.aia.AIAIntentionData;

@Component
public class AIAIntentionDataServiceImpl implements AIAIntentionDataService {

    private @Autowired AIAIntentionDataDao aiaIntentionDataDaoImpl;

    @Override
    public void createIntenionData(AIAIntentionData data) throws Exception {
        aiaIntentionDataDaoImpl.save(data);
    }

}
