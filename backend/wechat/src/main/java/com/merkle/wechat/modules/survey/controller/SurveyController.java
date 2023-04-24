package com.merkle.wechat.modules.survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.survey.Survey;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.survey.service.SurveyService;
import com.merkle.wechat.service.WechatPublicNoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "问卷管理")
@RequestMapping("/wechat/{channelId}/survey")
public class SurveyController extends AbstractController {
    private @Autowired SurveyService surveyServiceImpl;
    private @Autowired WechatPublicNoService publicNoServiceImpl;

    @NeedWrap
    @GetMapping("/answered/{openid}")
    @ApiOperation("获取参与过的问卷")
    public List<Survey> getAnsweredSurveys(@PathVariable String openid, @PathVariable Long channelId) throws Exception {
        return surveyServiceImpl.getAnsweredSurveys(channelId, openid);
    }

    @NeedWrap
    @GetMapping("/{id}")
    @ApiOperation("获取问卷")
    public Survey getSurvey(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        return surveyServiceImpl.findSurveyByWechatPublicNoIdAndId(channelId, id);
    }

    @NeedWrap
    @PostMapping
    @ApiOperation("创建问卷")
    public String createSurvey(@RequestBody Survey survey, @PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = publicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        surveyServiceImpl.createSurvey(survey, pbNo);
        return "ok";
    }     
    
}
