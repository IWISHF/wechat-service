package com.merkle.wechat.controller.analysis;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.merkle.wechat.controller.AbstractController;

import io.swagger.annotations.Api;

@Api(tags = "图文数据分析")
@Controller
@RequestMapping("/wechat/{channelId}/analysis/article")
public class ArticleDataController extends AbstractController {


}
