package com.merkle.wechat.common.dao.template;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.merkle.wechat.common.entity.template.WeixinTemplate;

@Repository
public interface WeixinTemplateMessageDao extends PagingAndSortingRepository<WeixinTemplate, Long> {

    List<WeixinTemplate> findByWechatPublicNoId(Long pbNoId);

    WeixinTemplate findOneByTemplateIdAndWechatPublicNoId(String templateId, Long pbNoId);

    Page<WeixinTemplate> findByWechatPublicNoIdAndTitleContaining(Long pbNoId, String key, Pageable pageable);

    List<WeixinTemplate> findByWechatPublicNoIdAndEnableAndTitleContaining(Long pbNoId, boolean enable, String key);

}
