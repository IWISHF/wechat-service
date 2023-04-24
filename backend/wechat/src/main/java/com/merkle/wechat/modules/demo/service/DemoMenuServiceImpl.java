package com.merkle.wechat.modules.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.service.TokenService;
import com.merkle.wechat.service.WechatPublicNoService;

import weixin.popular.api.MenuAPI;
import weixin.popular.bean.menu.Button;
import weixin.popular.bean.menu.Matchrule;
import weixin.popular.bean.menu.Menu;
import weixin.popular.bean.menu.MenuButtons;
import weixin.popular.bean.menu.TrymatchResult;

@Component
public class DemoMenuServiceImpl implements DemoMenuService {

    private @Autowired TokenService tokenServiceImpl;
    private @Autowired WechatPublicNoService wechatPbNoServiceImpl;

    public void restoreNanjingDefaultMenu() {
        String appId = "wx50f190ca3a0ee011";
        String defaultNanjingJson = "{\"button\":[{\"name\":\"产品信息\",\"sub_button\":[{\"type\":\"click\",\"name\":\"搜索零件\",\"key\":\"V1001_search\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"最新产品\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FPage%2Fjumppage%3Fpage%3Dlatest&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"产品分类\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FPage%2Fjumppage%3Fpage%3Dcategory&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"代理厂商\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FPage%2Fjumppage%3Fpage%3Dmanufacturer&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"扫一扫\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FScan%2Fscan&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]}]},{\"name\":\"设计支持\",\"sub_button\":[{\"type\":\"view\",\"name\":\"工程师锦囊\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FPage%2Fjumppage%3Fpage%3Dengineer&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"教学套件\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FPage%2Fjumppage%3Fpage%3Dteaching&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"文章资料\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FPage%2Fjumppage%3Fpage%3Dmaterial&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]},{\"type\":\"click\",\"name\":\"视频库\",\"key\":\"V1001_video\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"换算器\",\"url\":\"https:\\/\\/open.weixin.qq.com\\/connect\\/oauth2\\/authorize?appid=wx50f190ca3a0ee011&redirect_uri=http%3A%2F%2Fwechat4.merklechina.com%2Fwexin_dev%2Fdigikey_dev%2Fwechat.php%2FHome%2FPage%2Fjumppage%3Fpage%3Dscaler&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\"sub_button\":[]}]},{\"type\":\"click\",\"name\":\"消暑礼\",\"key\":\"V3001_xsl\",\"sub_button\":[]}]}";
        MenuAPI.menuDelete(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId));
        MenuAPI.menuCreate(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), defaultNanjingJson);
    }

    public void restoreMerkleDefaultMenu() {
        String appId = "wx9f5a246a8ff9c3f3";
        String defaultNanjingJson = "{\"button\":[{\"name\":\"公司资讯\",\"sub_button\":[{\"type\":\"view\",\"name\":\"我要开店\",\"url\":\"http:\\/\\/wechat5.merklechina.com\\/other\\/Game_Site_Selection\\/demo.html\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"新浪微博\",\"url\":\"http:\\/\\/weibo.com\\/merkleinc\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"优酷视频\",\"url\":\"http:\\/\\/i.youku.com\\/merkleinc\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"Linked In\",\"url\":\"https:\\/\\/www.linkedin.com\\/in\\/merkle-china-b5896588\\/\",\"sub_button\":[]}]},{\"name\":\"招聘和培训\",\"sub_button\":[{\"type\":\"view\",\"name\":\"社会招聘\",\"url\":\"http:\\/\\/m.51job.com\\/search\\/joblist.php?keyword=美库尔商务信息咨询有限公司&keywordtype=1\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"校园招聘\",\"url\":\"http:\\/\\/v6.rabbitpre.com\\/m\\/aUe1ZjDfY0?lc=3&sui=l0Iv7UUe&from=groupmessage&isappinstalled=0#from=share \",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"学员反馈\",\"url\":\"http:\\/\\/mp.weixin.qq.com\\/s?__biz=MjM5Nzc3ODkyMA==&mid=400544471&idx=5&sn=811a99fbf1f551a3218bf38525365437&scene=18\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"培训详情\",\"url\":\"https:\\/\\/mp.weixin.qq.com\\/s?__biz=MzA3MjE1MDMzOQ==&mid=2651944276&idx=6&sn=4fdeb9c48df14da7d8aa6367eaf2700e&chksm=84c76978b3b0e06e35119efbc661f7f0d65d95c727df00cee6982c1ea21a42a27dbe7f469b57&mpshare=1&scene=1&srcid=0213URWRkDwr9BD6Sm7J1BXb&pass_ticket=y+2g8pIFtPCanjcYimvB4rmUEHCEkhl\\/r9owkKqJpHZcsrxxtW0dYuMV6pNV2Xga#rd\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"培训简介\",\"url\":\"http:\\/\\/www.rabbitpre.com\\/m\\/AA27QAYBv?sui=mANmCSdH&lc=2#from=share\",\"sub_button\":[]}]},{\"name\":\"关于我们\",\"sub_button\":[{\"type\":\"view\",\"name\":\"公司概况\",\"url\":\"http:\\/\\/www.merklechina.cn\\/about-merkle.html\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"服务内容\",\"url\":\"http:\\/\\/www.merklechina.cn\\/precise-targeting-services.html#\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"解决方案\",\"url\":\"http:\\/\\/www.merklechina.cn\\/case-studies.html#\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"我们的客户\",\"url\":\"http:\\/\\/www.merklechina.cn\\/about-merkle.html#our-client\",\"sub_button\":[]},{\"type\":\"view\",\"name\":\"联系我们\",\"url\":\"http:\\/\\/www.merklechina.cn\\/contact-us.html\",\"sub_button\":[]}]}]}";
        MenuAPI.menuDelete(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId));
        MenuAPI.menuCreate(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), defaultNanjingJson);
    }

    public void createMerkleMenue() {
        String appId = "wx9f5a246a8ff9c3f3";
        String demoJson = "{\n" + "    \"button\": [\n" + "        {\n" + "            \"name\": \"活动\",\n"
                + "            \"type\": \"view\",\n" + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play One\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p1\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play Five\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p5\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n"
                + "        },\n" + "        {\n" + "            \"type\": \"view\",\n"
                + "            \"name\": \"商城\",\n"
                + "            \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/introduce.html\",\n"
                + "            \"sub_button\": []\n" + "        },\n" + "        {\n"
                + "            \"type\": \"view\",\n" + "            \"name\": \"会员\",\n"
                + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员中心\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/mc.html\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    ],\n" + "    \"matchrule\": {\n" + "        \"group_id\": 104\n" + "    }\n" + "}";
        String p1Json = "{\n" + "    \"button\": [\n" + "        {\n" + "            \"name\": \"活动\",\n"
                + "            \"type\": \"view\",\n" + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play One\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p1\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play Five\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p5\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n"
                + "        },\n" + "        {\n" + "            \"type\": \"view\",\n"
                + "            \"name\": \"商城\",\n"
                + "            \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/introduce.html\",\n"
                + "            \"sub_button\": []\n" + "        },\n" + "        {\n"
                + "            \"type\": \"view\",\n" + "            \"name\": \"会员\",\n"
                + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员中心\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/mc.html\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠券\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=coupon\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"奖励\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=awards\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠活动\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=offers\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    ],\n" + "    \"matchrule\": {\n" + "        \"group_id\": 102\n" + "    }\n" + "}";
        String p5Json = "{\n" + "    \"button\": [\n" + "        {\n" + "            \"name\": \"活动\",\n"
                + "            \"type\": \"view\",\n" + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play One\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p1\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play Five\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p5\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n"
                + "        },\n" + "        {\n" + "            \"type\": \"view\",\n"
                + "            \"name\": \"商城\",\n"
                + "            \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/introduce.html\",\n"
                + "            \"sub_button\": []\n" + "        },\n" + "        {\n"
                + "            \"type\": \"view\",\n" + "            \"name\": \"会员\",\n"
                + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员中心\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/mc.html\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员详情\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/detail.html\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠券\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=coupon\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"奖励\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=awards\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠活动\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/3/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=offers\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    ],\n" + "    \"matchrule\": {\n" + "        \"group_id\": 103\n" + "    }\n" + "}";
        // MenuAPI.menuDelete(tokenDaoImpl.findByAppId(appId).getAccessToken());
        // MenuAPI.menuCreate(tokenDaoImpl.findByAppId(appId).getAccessToken(),
        // defaultJson);
        MenuAPI.menuAddconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), demoJson);
        MenuAPI.menuAddconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), p1Json);
        MenuAPI.menuAddconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), p5Json);
    }

    public void createNanjingMenue() {
        String appId = "wx50f190ca3a0ee011";
        String demoJson = "{\n" + "    \"button\": [\n" + "        {\n" + "            \"name\": \"活动\",\n"
                + "            \"type\": \"view\",\n" + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play One\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p1\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play Five\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p5\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n"
                + "        },\n" + "        {\n" + "            \"type\": \"view\",\n"
                + "            \"name\": \"商城\",\n"
                + "            \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/introduce.html\",\n"
                + "            \"sub_button\": []\n" + "        },\n" + "        {\n"
                + "            \"type\": \"view\",\n" + "            \"name\": \"会员\",\n"
                + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员中心\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/mc.html\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    ],\n" + "    \"matchrule\": {\n" + "        \"group_id\": 148\n" + "    }\n" + "}";
        String p1Json = "{\n" + "    \"button\": [\n" + "        {\n" + "            \"name\": \"活动\",\n"
                + "            \"type\": \"view\",\n" + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play One\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p1\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play Five\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p5\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n"
                + "        },\n" + "        {\n" + "            \"type\": \"view\",\n"
                + "            \"name\": \"商城\",\n"
                + "            \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/introduce.html\",\n"
                + "            \"sub_button\": []\n" + "        },\n" + "        {\n"
                + "            \"type\": \"view\",\n" + "            \"name\": \"会员\",\n"
                + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员中心\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/mc.html\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠券\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=coupon\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"奖励\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=awards\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠活动\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=offers\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    ],\n" + "    \"matchrule\": {\n" + "        \"group_id\": 141\n" + "    }\n" + "}";
        String p5Json = "{\n" + "    \"button\": [\n" + "        {\n" + "            \"name\": \"活动\",\n"
                + "            \"type\": \"view\",\n" + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play One\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p1\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"Play Five\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p5\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n"
                + "        },\n" + "        {\n" + "            \"type\": \"view\",\n"
                + "            \"name\": \"商城\",\n"
                + "            \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/introduce.html\",\n"
                + "            \"sub_button\": []\n" + "        },\n" + "        {\n"
                + "            \"type\": \"view\",\n" + "            \"name\": \"会员\",\n"
                + "            \"sub_button\": [\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员中心\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/mc.html\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"会员详情\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/detail.html\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠券\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=coupon\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"奖励\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=awards\",\n"
                + "                    \"sub_button\": []\n" + "                },\n" + "                {\n"
                + "                    \"type\": \"view\",\n" + "                    \"name\": \"优惠活动\",\n"
                + "                    \"url\": \"https://lp-cn1-wechat-production.merklechina.com/wechat/2/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/promotion.html?type=offers\",\n"
                + "                    \"sub_button\": []\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    ],\n" + "    \"matchrule\": {\n" + "        \"group_id\": 147\n" + "    }\n" + "}";
        // MenuAPI.menuDelete(tokenDaoImpl.findByAppId(appId).getAccessToken());
        // MenuAPI.menuCreate(tokenDaoImpl.findByAppId(appId).getAccessToken(),
        // defaultJson);
        MenuAPI.menuAddconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), demoJson);
        MenuAPI.menuAddconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), p1Json);
        MenuAPI.menuAddconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), p5Json);
    }

    // TODO : 重构代码
    public void createMenu(String appId, String tagid) {
        WechatPublicNo pbNo = wechatPbNoServiceImpl.findOneByAuthorizerAppid(appId);
        MenuButtons buttons = new MenuButtons();
        Button product = new Button();
        product.setType("view");
        product.setUrl("https://lp-cn1-wechat-production.merklechina.com/wechat/" + pbNo.getId()
                + "/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/product.html?type=p1");
        product.setName("产品");
        buttons.setButton(new Button[] { product });
        Button learn = new Button();
        learn.setType("view");
        learn.setUrl("https://lp-cn1-wechat-production.merklechina.com/wechat/" + pbNo.getId()
                + " /oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/introduce.html");
        learn.setName("学习");
        Button vip = new Button();
        vip.setType("view");
        vip.setUrl("https://lp-cn1-wechat-production.merklechina.com/wechat/" + pbNo.getId()
                + "/oauth?redirect=https://lp-cn1-wechat-production.merklechina.com/h5/mc.html");
        vip.setName("会员");
        buttons.setButton(new Button[] { product, learn, vip });
        Matchrule rule = new Matchrule();
        rule.setTag_id(tagid);
        rule.setGroup_id(Integer.valueOf(tagid));
        buttons.setMatchrule(rule);
        MenuAPI.menuAddconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), buttons);
    }

    public Menu getMenue(String appId) {
        return MenuAPI.menuGet(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId));
    }

    public TrymatchResult tagTryMatchMenu(String openid, String appId) {
        return MenuAPI.menuTrymatch(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), openid);
    }

    @Override
    public void delMenue(String menuId, String appId) {
        MenuAPI.menuDelconditional(tokenServiceImpl.getPublicNoAccessTokenByAppId(appId), menuId);
    }
}
