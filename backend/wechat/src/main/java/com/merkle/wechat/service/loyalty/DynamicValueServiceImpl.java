package com.merkle.wechat.service.loyalty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.template.WeixinExpressTemplateConfig;
import com.merkle.wechat.common.entity.template.WeixinNoticeTemplateConfig;

@Component
public class DynamicValueServiceImpl implements DynamicValueService {
    private final String NICKNAME = "粉丝Nickname";
    private final String COUPON_CODE = "虚拟兑换码";
    private final String REWARD_NAME = "奖励名称";
    private final String CURRENT_DATE = "当前日期";
    private final String TRACKING_CODE = "单号";

    @Override
    public List<String> dynamicValue() {
        List<String> dynamicValues = new ArrayList<>();
        dynamicValues.add(NICKNAME);
        dynamicValues.add(COUPON_CODE);
        dynamicValues.add(REWARD_NAME);
        dynamicValues.add(CURRENT_DATE);
        dynamicValues.add(TRACKING_CODE);
        return dynamicValues;
    }

    @Override
    public String getValue(WeixinNoticeTemplateConfig wc, Follower follower, RewardsRedeemLog redeemLog) {
        // specific value
        if (wc.getType() == 1) {
            return wc.getFieldValue();
        }
        String value = "";
        switch (wc.getFieldValue()) {
            case NICKNAME:
                value = follower.getNickname();
                break;
            case COUPON_CODE:
                value = redeemLog.getCouponCode();
                break;
            case REWARD_NAME:
                value = redeemLog.getRewardName();
                break;
            case CURRENT_DATE:
                value = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
        }
        return value;
    }

    @Override
    public String getValueForExpress(WeixinExpressTemplateConfig wc, Follower follower, RewardsRedeemLog redeemLog) {
        if (wc.getType() == 1) {
            return wc.getFieldValue();
        }
        String value = "";
        switch (wc.getFieldValue()) {
            case NICKNAME:
                value = follower.getNickname();
                break;
            case COUPON_CODE:
                value = redeemLog.getCouponCode();
                break;
            case REWARD_NAME:
                value = redeemLog.getRewardName();
                break;
            case CURRENT_DATE:
                value = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            case TRACKING_CODE:
                value = redeemLog.getTrackingCode();
                break;
        }
        return value;
    }

}
