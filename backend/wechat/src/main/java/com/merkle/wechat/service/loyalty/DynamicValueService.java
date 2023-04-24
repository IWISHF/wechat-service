package com.merkle.wechat.service.loyalty;

import java.util.List;

import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.template.WeixinExpressTemplateConfig;
import com.merkle.wechat.common.entity.template.WeixinNoticeTemplateConfig;

public interface DynamicValueService {

    List<String> dynamicValue();

    String getValue(WeixinNoticeTemplateConfig wc, Follower follower, RewardsRedeemLog redeemLog);

    String getValueForExpress(WeixinExpressTemplateConfig wc, Follower follower, RewardsRedeemLog redeemLog);

}
