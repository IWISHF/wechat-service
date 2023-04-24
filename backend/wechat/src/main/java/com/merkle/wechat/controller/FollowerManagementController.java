package com.merkle.wechat.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.merkle.wechat.annotation.NeedWrap;
import com.merkle.wechat.common.entity.ExportLog;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.constant.ExceptionConstants;
import com.merkle.wechat.modules.loyalty.service.LoyaltyService;
import com.merkle.wechat.service.WechatPublicNoService;
import com.merkle.wechat.service.follower.FollowerBindInfoService;
import com.merkle.wechat.service.follower.FollowerService;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.follower.FollowerSearchVo;
import com.merkle.wechat.vo.follower.FollowerTagResult;
import com.merkle.wechat.vo.follower.FollowerTagVo;
import com.merkle.wechat.vo.follower.FollowerVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "粉丝管理")
@RequestMapping("/wechat/{channelId}/management/follower")
public class FollowerManagementController extends AbstractController {
    private @Autowired FollowerService followerServiceImpl;
    private @Autowired WechatPublicNoService pbNoServiceImpl;
    private @Autowired LoyaltyService loyaltyServiceImpl;
    private @Autowired FollowerBindInfoService followerBindInfoServiceImpl;

    @NeedWrap
    @GetMapping("/{id}")
    @ApiOperation("获取粉丝详情")
    public FollowerVo getFollowerByOpenId(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);

        return followerServiceImpl.getFollowerByAppIdAndId(pbNo, id);
    }

    @NeedWrap
    @GetMapping("/search")
    @ApiOperation(value = "根据nickname查找粉丝")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<FollowerVo> searchFollowerByNickName(@PathVariable Long channelId,
            @RequestParam(defaultValue = "") String key, @ApiIgnore Pageable pageable) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return followerServiceImpl.searchByNickname(pbNo, Base64Utils.encodeToString(key.getBytes()), pageable);
    }

    @NeedWrap
    @PostMapping("/search/advance")
    @ApiOperation("多条件组合搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "当前页面编号(0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "页面大小最大2000"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "排序，格式: property(,asc|desc). 默认升序.多排序支持.") })
    public Pagination<FollowerVo> searchByMultiCondition(@PathVariable Long channelId,
            @RequestBody @Valid FollowerSearchVo condition, @ApiIgnore Pageable pageable) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return followerServiceImpl.searchByMultiCondition(condition, pbNo, pageable);
    }

    @NeedWrap
    @PostMapping("/search/advance/count")
    @ApiOperation("多条件组合搜索全部粉丝数量")
    public Long countByMultiCondition(@PathVariable Long channelId, @RequestBody @Valid FollowerSearchVo condition)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        condition.setSubscribe(1);
        return followerServiceImpl.countByMultiCondition(condition, pbNo);
    }

    @NeedWrap
    @PostMapping("/export")
    @ApiOperation("多条件组合导出粉丝信息")
    public String exportByMultiCondition(@PathVariable Long channelId, @RequestBody @Valid FollowerSearchVo condition)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return followerServiceImpl.exportByMultiCondition(condition, pbNo);
    }

    @NeedWrap
    @GetMapping("/export/polling/{id}")
    @ApiOperation("轮询导出任务结果")
    public ExportLog exportPollingCheck(@PathVariable Long channelId, @PathVariable Long id) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        ExportLog log = followerServiceImpl.exportPollingCheckResult(id, pbNo);
        if (log != null) {
            log.setPath(log.getId() + "");
        }
        return log;
    }

    @GetMapping("export/file")
    @ApiOperation("获取导出的粉丝信息文件")
    public void readFile(String path, @PathVariable Long channelId, HttpServletResponse response) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        ExportLog log = followerServiceImpl.exportPollingCheckResult(Long.valueOf(path), pbNo);
        if (log != null && log.getStatus().equals(ExportLog.FINISHED)) {
            path = log.getPath();
        } else {
            throw new ServiceWarn(ExceptionConstants.NOT_EXIST);
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new ServiceWarn(ExceptionConstants.NOT_EXIST);
        }
        BufferedInputStream ins = new BufferedInputStream(new FileInputStream(file));
        ServletOutputStream out = response.getOutputStream();
        String fileType = path.split("\\.")[1];
        switch (fileType) {
            case "xlsx":
                response.setHeader("content-type", "application/vnd.ms-excel");
                break;
        }
        String[] strs = path.split("/");
        response.addHeader("content-disposition",
                "attachment;filename=" + new String(strs[strs.length - 1].getBytes(), "ISO8859-1"));

        int len = 0;
        byte[] b = new byte[2048];
        while ((len = ins.read(b)) > 0) {
            out.write(b, 0, len);
        }
        out.flush();
        out.close();
        ins.close();
    }

    @NeedWrap
    @PostMapping("/tag/add")
    @ApiOperation("打标签")
    public List<FollowerTagResult> tagFollower(@PathVariable Long channelId, @RequestBody @Valid FollowerTagVo vo)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        return followerServiceImpl.tagFollowers(vo.getFollowerIds(), vo.getTags(), pbNo);
    }

    @NeedWrap
    @PutMapping("/tag/remove/{id}")
    @ApiOperation("删除标签")
    public String tagRemove(@PathVariable Long channelId, @PathVariable Long id, @RequestBody Set<Tag> tagsNeedToRemove)
            throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        followerServiceImpl.removeTagFromFollower(tagsNeedToRemove, id, pbNo);
        return "ok";
    }

    @NeedWrap
    @GetMapping("/sync")
    @ApiOperation("同步粉丝数据到Loyalty")
    public String syncFollowerToLoyalty(@PathVariable Long channelId, @RequestParam String start) throws Exception {
        WechatPublicNo pbNo = pbNoServiceImpl.findByIdOrThrowNotExistException(channelId);
        if (start.equals("now")) {
            loyaltyServiceImpl.syncFollowerToLoyalty(pbNo);
        }
        return "ok";
    }

    @NeedWrap
    @GetMapping("/sync/bindInfo")
    @ApiOperation("同步粉丝bindInfo数据到Loyalty")
    public String syncBindInfoToLoyalty(@RequestParam String start) throws Exception {
        if (start.equals("now")) {
            followerBindInfoServiceImpl.fixSyncFollowerInfoFailed();
        }
        return "ok";
    }

}
