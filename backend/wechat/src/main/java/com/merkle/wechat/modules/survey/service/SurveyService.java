package com.merkle.wechat.modules.survey.service;

import java.util.List;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.survey.Survey;

public interface SurveyService {

    List<Survey> getAnsweredSurveys(Long channelId, String openid) throws Exception;

    Survey findSurveyByWechatPublicNoIdAndId(Long channelId, Long surveyId);

    void createSurvey(Survey survey, WechatPublicNo pbNo);

}
