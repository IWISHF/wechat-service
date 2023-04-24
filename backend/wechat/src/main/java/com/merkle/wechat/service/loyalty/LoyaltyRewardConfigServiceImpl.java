package com.merkle.wechat.service.loyalty;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.loyalty.LoyaltyRewardConfigDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.loyalty.LoyaltyRewardConfig;
import com.merkle.wechat.common.entity.template.WeixinNoticeTemplateConfig;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.vo.Pagination;

@Component
public class LoyaltyRewardConfigServiceImpl implements LoyaltyRewardConfigService {
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired LoyaltyRewardConfigDao loyaltyRewardConfigDaoImpl;

    @Override
    public void create(Long channelId, LoyaltyRewardConfig config) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        LoyaltyRewardConfig dbConfig = loyaltyRewardConfigDaoImpl.findByWechatPublicNoIdAndRewardId(channelId,
                config.getRewardId());
        if (dbConfig != null) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }

        if (config.getRewardType().equals(LoyaltyRewardConfig.IN_KIND)
                && (StringUtils.isEmpty(config.getExpressTemplateId()) || config.getExpressTemplateConfigs().isEmpty()
                        || StringUtils.isEmpty(config.getExpressCompany()))) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }

        config.setEnable(true);
        config.setWechatPublicNoId(channelId);
        config.setAppId(pbNo.getAuthorizerAppid());
        loyaltyRewardConfigDaoImpl.save(config);
    }

    @Override
    public void update(Long channelId, LoyaltyRewardConfig config) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        LoyaltyRewardConfig dbConfig = loyaltyRewardConfigDaoImpl.findByWechatPublicNoIdAndRewardId(channelId,
                config.getRewardId());
        if (dbConfig == null) {
            throw new ServiceWarn(ExceptionConstants.NOT_EXIST);
        }

        if (dbConfig.getId() != config.getId()) {
            throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
        }

        if (config.getRewardType().equals(LoyaltyRewardConfig.IN_KIND)
                && (StringUtils.isEmpty(config.getExpressTemplateId()) || config.getExpressTemplateConfigs().isEmpty()
                        || StringUtils.isEmpty(config.getExpressCompany()))) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }

        dbConfig.setNoticeTemplateId(config.getNoticeTemplateId());
        Set<WeixinNoticeTemplateConfig> noticeTemplateConfigs = dbConfig.getNoticeTemplateConfigs();
        noticeTemplateConfigs.clear();
        noticeTemplateConfigs.addAll(config.getNoticeTemplateConfigs());

        if (config.getRewardType().equals(LoyaltyRewardConfig.IN_KIND)) {
            dbConfig.setExpressTemplateId(config.getExpressTemplateId());
            dbConfig.getExpressTemplateConfigs().clear();
            dbConfig.getExpressTemplateConfigs().addAll(config.getExpressTemplateConfigs());
        }
        dbConfig.setEnable(true);
        dbConfig.setRewardId(config.getRewardId());
        dbConfig.setRewardName(config.getRewardName());
        dbConfig.setRewardType(config.getRewardType());
        dbConfig.setWechatPublicNoId(channelId);
        dbConfig.setAppId(pbNo.getAuthorizerAppid());
        dbConfig.setExpressCompany(config.getExpressCompany());

        loyaltyRewardConfigDaoImpl.save(dbConfig);
    }

    @Override
    public void delete(Long channelId, Long id) {
        loyaltyRewardConfigDaoImpl.deleteByWechatPublicNoIdAndId(channelId, id);
    }

    @Override
    public Pagination<LoyaltyRewardConfig> search(Long channelId, String key, Pageable pageable) {
        Page<LoyaltyRewardConfig> page = loyaltyRewardConfigDaoImpl
                .findByWechatPublicNoIdAndRewardNameContaining(channelId, key, pageable);
        return new Pagination<>(page);
    }

    @Override
    public LoyaltyRewardConfig findOne(Long channelId, Long id) {
        LoyaltyRewardConfig config = loyaltyRewardConfigDaoImpl.findOneByWechatPublicNoIdAndId(channelId, id);
        if (config == null) {
            throw new ServiceWarn(ExceptionConstants.NOT_EXIST);
        }

        return config;
    }

}
