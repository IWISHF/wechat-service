package com.merkle.wechat.modules.survey.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.merkle.wechat.common.dao.survey.SurveyAnswerDao;
import com.merkle.wechat.common.dao.survey.SurveyDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.survey.Survey;
import com.merkle.wechat.common.entity.survey.SurveyAnswer;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;

@Component
public class SurveyServiceImpl implements SurveyService {
    private @Autowired SurveyDao surveyDaoImpl;
    private @Autowired SurveyAnswerDao surveyAnswerDaoImpl;

    @Override
    public List<Survey> getAnsweredSurveys(Long channelId, String openid) throws Exception {
        List<Long> ids = new ArrayList<>();
        List<SurveyAnswer> answers = surveyAnswerDaoImpl.findByOpenidAndWechatPublicNoId(openid, channelId);
        if (answers != null && answers.size() > 0) {
            ids.addAll(
                    answers.stream().mapToLong((m) -> m.getSurveyId()).distinct().boxed().collect(Collectors.toList()));
        }

        if (ids.size() > 0) {
            List<Survey> surveys = (List<Survey>) surveyDaoImpl.findAll(ids);
            surveys.sort((a, b) -> {
                return a.getId() > b.getId() ? -1 : 1;
            });
            return surveys;
        }

        return new ArrayList<>();
    }

    @Override
    public Survey findSurveyByWechatPublicNoIdAndId(Long channelId, Long id) {
        return surveyDaoImpl.findByWechatPublicNoIdAndId(channelId, id);
    }

    @Override
    public void createSurvey(Survey survey, WechatPublicNo pbNo) {
        if (survey.isEnableRecordEvent() && StringUtils.isEmpty(survey.getEventType())) {
            throw new ServiceWarn(ExceptionConstants.PARAM_ERROR);
        }
        survey.setWechatPublicNoId(pbNo.getId());
        surveyDaoImpl.save(survey);
    }

}
