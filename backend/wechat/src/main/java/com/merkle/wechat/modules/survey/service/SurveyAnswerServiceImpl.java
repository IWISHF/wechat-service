package com.merkle.wechat.modules.survey.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.merkle.loyalty.response.PrismResponse;
import com.merkle.loyalty.util.JsonUtil;
import com.merkle.wechat.common.dao.survey.SurveyAnswerDao;
import com.merkle.wechat.common.dao.survey.SurveyDao;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.survey.Survey;
import com.merkle.wechat.common.entity.survey.SurveyAnswer;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;

@Component
public class SurveyAnswerServiceImpl implements SurveyAnswerService {
    private @Autowired SurveyAnswerDao surveyAnswerDaoImpl;
    private @Autowired SurveyDao surveyDaoImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;

    @Override
    public void createSurveyAnswer(SurveyAnswer answer, WechatPublicNo pbNo) throws Exception {
        Survey s = surveyDaoImpl.findOne(answer.getSurveyId());
        Date currentDate = new Date();

        if ((s.getEndDate() != null && currentDate.after(s.getEndDate())) || currentDate.before(s.getStartDate())
                || !s.isEnable()) {
            throw new ServiceWarn(ExceptionConstants.SURVEY_CANT_ACCESS_NOW);
        }

        if (!s.isMulti()) {
            boolean exist = alreadyAnswerd(answer.getSurveyId(), answer.getOpenid(), pbNo.getId());
            if (exist) {
                throw new ServiceWarn(ExceptionConstants.ALREADY_EXIST);
            }
        }

        if (s.isEnableRecordEvent()) {
            PrismResponse response = null;
            try {
                response = loyaltyServiceImpl.recordEvent(answer.getOpenid(), pbNo.getAuthorizerAppid(),
                        s.getEventType());
                JsonNode jsonNode = JsonUtil.readTree(response.toString());
                boolean isSuccess = jsonNode.get("success").asBoolean();
                answer.setEventRecordStatus(isSuccess);
                answer.setEventRecordResponse(response.toString());

            } catch (Exception e) {
                answer.setEventRecordStatus(false);
                throw e;
            }
        }

        answer.setWechatPublicNoId(pbNo.getId());
        answer.setCreatedDate(new Date());
        answer.getQuestions().forEach((question) -> {
            question.getOptions().forEach((option) -> {
                option.setCreatedDate(new Date());
            });
            question.setCreatedDate(new Date());
        });

        surveyAnswerDaoImpl.save(answer);
    }

    @Override
    public boolean alreadyAnswerd(Long surveyId, String openid, Long channelId) throws Exception {
        return surveyAnswerDaoImpl.existsByOpenidAndSurveyIdAndWechatPublicNoId(openid, surveyId, channelId);
    }

    @Override
    public SurveyAnswer getAnswer(Long surveyId, String openid, Long channelId) throws Exception {
        return surveyAnswerDaoImpl.findFirstByOpenidAndSurveyIdAndWechatPublicNoId(openid, surveyId, channelId);
    }

    @Override
    public void updateSurveyAnswer(SurveyAnswer answer, WechatPublicNo pbNo) throws Exception {
        SurveyAnswer answerCheck = surveyAnswerDaoImpl.findBySurveyAnswerId(answer.getSurveyAnswerId());
        Optional.ofNullable(answerCheck).orElseThrow(() -> new ServiceWarn("Error survey Answer"));

        Survey s = surveyDaoImpl.findOne(answer.getSurveyId());
        Date currentDate = new Date();

        if (!s.isEnableEdit()) {
            throw new ServiceWarn("Survey answer can't be edit");
        }

        if ((s.getEndDate() != null && currentDate.after(s.getEndDate())) || currentDate.before(s.getStartDate())
                || !s.isEnable()) {
            throw new ServiceWarn(ExceptionConstants.SURVEY_CANT_ACCESS_NOW);
        }


        if (s.isEnableRecordEvent() && !answer.isEventRecordStatus()) {
            PrismResponse response = null;
            try {
                response = loyaltyServiceImpl.recordEvent(answer.getOpenid(), pbNo.getAuthorizerAppid(),
                        s.getEventType());
                JsonNode jsonNode = JsonUtil.readTree(response.toString());
                boolean isSuccess = jsonNode.get("success").asBoolean();
                answer.setEventRecordStatus(isSuccess);
                answer.setEventRecordResponse(response.toString());

            } catch (Exception e) {
                answer.setEventRecordStatus(false);
                throw e;
            }
        }

        answer.setWechatPublicNoId(pbNo.getId());
        answer.setUpdatedDate(currentDate);
        answer.getQuestions().forEach((question) -> {
            question.getOptions().forEach((option) -> {
                option.setCreatedDate(new Date());
            });
            question.setCreatedDate(new Date());
        });

        surveyAnswerDaoImpl.save(answer);
    }

    @Override
    public int countByIdAndCreatedDateBetween(Long surveyId, Date startOfDay, Date endOfDay) {
        return surveyAnswerDaoImpl.countBySurveyIdAndCreatedDateBetween(surveyId, startOfDay, endOfDay);
    }
}
