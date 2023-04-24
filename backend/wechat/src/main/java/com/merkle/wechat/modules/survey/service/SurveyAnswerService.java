package com.merkle.wechat.modules.survey.service;

import java.util.Date;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.survey.SurveyAnswer;

public interface SurveyAnswerService {

    void createSurveyAnswer(SurveyAnswer answer, WechatPublicNo pbNo) throws Exception;

    void updateSurveyAnswer(SurveyAnswer answer, WechatPublicNo pbNo) throws Exception;

    boolean alreadyAnswerd(Long surveyId, String openid, Long channelId) throws Exception;

    SurveyAnswer getAnswer(Long surveyId, String openid, Long channelId) throws Exception;

    int countByIdAndCreatedDateBetween(Long surveyId, Date startOfDay, Date endOfDay);

}
