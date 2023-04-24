package com.merkle.wechat.service.follower;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.merkle.wechat.common.entity.ExportLog;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.vo.Pagination;
import com.merkle.wechat.vo.follower.FollowerCheckVo;
import com.merkle.wechat.vo.follower.FollowerRelatedVo;
import com.merkle.wechat.vo.follower.FollowerSearchVo;
import com.merkle.wechat.vo.follower.FollowerTagResult;
import com.merkle.wechat.vo.follower.FollowerVo;

public interface FollowerService {

    String getFollowerUnionId(String openid);

    Follower findOrCreateFollower(String openid, String appId) throws Exception;

    Follower createFollower(String openid, String appId) throws Exception;

    Follower updateFollower(Follower follower) throws Exception;

    Follower unsubscribeFollower(String openid) throws Exception;

    void syncFollowersFromWechat(String appId) throws Exception;

    Follower findOneByOpenid(String openid);

    Follower syncLatestFollowerInfo(Follower follower) throws Exception;

    Set<String> findDistinctOpenIdsByCondition(String appId, String province);

    Follower getFollowerByAppIdAndOpenid(WechatPublicNo pbNo, String openid);

    Pagination<FollowerVo> searchByNickname(WechatPublicNo pbNo, String nickname, Pageable pageable);

    FollowerVo getFollowerByAppIdAndId(WechatPublicNo pbNo, Long id);

    Pagination<FollowerVo> searchByMultiCondition(FollowerSearchVo condition, WechatPublicNo pbNo, Pageable pageable);

    List<FollowerTagResult> tagFollowers(List<Long> followerIds, Set<Tag> tags, WechatPublicNo pbNo);

    void removeTagFromFollower(Set<Tag> tagsNeedToRemove, Long id, WechatPublicNo pbNo);

    String exportByMultiCondition(FollowerSearchVo condition, WechatPublicNo pbNo) throws Exception;

    ExportLog exportPollingCheckResult(Long id, WechatPublicNo pbNo);

    void tagFollower(String fromUserName, Set<Tag> tags, String pbNoAppId);

    Long countByMultiCondition(FollowerSearchVo condition, WechatPublicNo pbNo);

    FollowerVo getFollowerInfo(WechatPublicNo pbNo, String id);

    FollowerRelatedVo checkFollowerRelated(FollowerCheckVo filters, String openid) throws Exception;

}
