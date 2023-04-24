package com.merkle.wechat.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.util.TimeUtil;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.statistics.FollowerStatisticsService;
import com.merkle.wechat.service.statistics.QrcodeStatisticsService;
import com.merkle.wechat.service.statistics.TagStatisticsService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.statistics.FollowerStatisticsItem;
import com.merkle.wechat.vo.statistics.FollowerStatisticsVo;
import com.merkle.wechat.vo.statistics.QrcodeStatisticsItem;
import com.merkle.wechat.vo.statistics.QrcodeStatisticsVo;
import com.merkle.wechat.vo.statistics.TagStatisticsVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "统计")
@RequestMapping("/wechat/{channelId}/statistics/")
public class StatisticsController extends AbstractController {
    private @Autowired QrcodeStatisticsService qrcodeStatisticsServiceImpl;
    private @Autowired TagStatisticsService tagStatisticsServiceImpl;
    private @Autowired FollowerStatisticsService followerStatisticsServiceImpl;
    private @Autowired WechatPublicNoService wechatPublicNoServiceImpl;

    @NeedWrap
    @GetMapping("/qrcode/search")
    @ApiOperation(value = "根据name搜索二维码统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<QrcodeStatisticsVo> search(@PathVariable Long channelId,
            @RequestParam(defaultValue = "") String key, @ApiIgnore Pageable pageable) throws Exception {
        WechatPublicNo pbNo = wechatPublicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return qrcodeStatisticsServiceImpl.searchByName(pbNo, key, pageable);
    }

    @NeedWrap
    @GetMapping("/qrcode/detail")
    @ApiOperation(value = "二维码统计根据日期查询")
    public Map<String, QrcodeStatisticsItem> statisticsByDateDetail(@PathVariable Long channelId,
            @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
            @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end,
            @RequestParam(required = true) Long qrcodeId) throws Exception {
        WechatPublicNo pbNo = wechatPublicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return qrcodeStatisticsServiceImpl.statisticsByDateDetail(pbNo, TimeUtil.formatYYYYMMDD(start.getTime()),
                TimeUtil.formatYYYYMMDD(end.getTime()), qrcodeId);
    }

    @NeedWrap
    @GetMapping("/follower/detail")
    @ApiOperation(value = "粉丝统计根据日期查询")
    public Map<String, FollowerStatisticsItem> statisticsFollowerByDateDetail(@PathVariable Long channelId,
            @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
            @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end,
            @RequestParam(defaultValue = "1") int type) throws Exception {
        WechatPublicNo pbNo = wechatPublicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return followerStatisticsServiceImpl.statisticsByDateDetail(pbNo, TimeUtil.formatYYYYMMDD(start.getTime()),
                TimeUtil.formatYYYYMMDD(end.getTime()), type);
    }

    @NeedWrap
    @GetMapping("/follower")
    @ApiOperation(value = "粉丝今日和昨日的统计信息")
    public FollowerStatisticsVo statisticsFollowerByDate(@PathVariable Long channelId) throws Exception {
        WechatPublicNo pbNo = wechatPublicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return followerStatisticsServiceImpl.statisticsByDate(pbNo);
    }

    @NeedWrap
    @GetMapping("/tag/search")
    @ApiOperation(value = "根据name搜索标签统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public TagStatisticsVo searchForTag(@PathVariable Long channelId, @RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "-1") int fromWechat, @ApiIgnore Pageable pageable) throws Exception {
        WechatPublicNo pbNo = wechatPublicNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return tagStatisticsServiceImpl.searchTagByName(pbNo, key, fromWechat, pageable);
    }

}
