package com.merkle.wechat.common.dao.survey;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.survey.Survey;

@Repository
public interface SurveyDao extends PagingAndSortingRepository<Survey, Long> {

    Survey findByWechatPublicNoIdAndId(Long channelId, Long id);

}
