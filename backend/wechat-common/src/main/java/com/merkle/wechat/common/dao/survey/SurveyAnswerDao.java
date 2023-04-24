package com.merkle.wechat.common.dao.survey;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.survey.SurveyAnswer;

@Repository
public interface SurveyAnswerDao extends PagingAndSortingRepository<SurveyAnswer, Long> {

    List<SurveyAnswer> findByOpenidAndWechatPublicNoId(String openid, Long channelId);

    boolean existsByOpenidAndSurveyIdAndWechatPublicNoId(String openid, Long surveyId, Long channelId);

    SurveyAnswer findFirstByOpenidAndSurveyIdAndWechatPublicNoId(String openid, Long surveyId, Long channelId);

    SurveyAnswer findBySurveyAnswerId(Long surveyAnswerId);

    int countBySurveyIdAndCreatedDateBetween(Long surveyId, Date startOfDay, Date endOfDay);

}
