package com.merkle.wechat.modules.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.survey.SurveyAnswer;
import com.merkle.wechat.controller.AbstractController;
import com.merkle.wechat.modules.survey.service.SurveyAnswerService;
import com.merkle.wechat.service.WechatPublicNoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "问卷参与API")
@RequestMapping("/wechat/{channelId}/survey/answer")
public class SurveyAnswerController extends AbstractController {
    private @Autowired SurveyAnswerService surveyAnswerServiceImpl;
    private @Autowired WechatPublicNoServiceImpl publicNoServiceImpl;

    @NeedWrap
    @PostMapping
    @ApiOperation("参与活动")
    public String createSurveyAnswer(@RequestBody SurveyAnswer answer, @PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = publicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        if (answer.getSurveyAnswerId() != null) {
            surveyAnswerServiceImpl.updateSurveyAnswer(answer, pbNo);
        } else {
            surveyAnswerServiceImpl.createSurveyAnswer(answer, pbNo);
        }
        return "ok";
    }

    @NeedWrap
    @GetMapping("/{surveyId}/check")
    @ApiOperation("是否参与活动")
    public SurveyAnswer alreadyAnswered(@PathVariable Long surveyId, @RequestParam(required = true) String openid,
            @PathVariable Long channelId) throws Exception {
        return surveyAnswerServiceImpl.getAnswer(surveyId, openid, channelId);
    }
}
