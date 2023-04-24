package com.merkle.wechat.modules.digikey.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.digikey.DigikeyLikeOrUnlikeRecordDao;
import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.WechatPublicNo;
import com.merkle.wechat.common.entity.digikey.DigikeyLikeOrUnlikeRecord;
import com.merkle.wechat.common.exception.ServiceWarn;
import com.merkle.wechat.modules.digikey.vo.LikeOrUnlikeStatisticsVo;
import com.merkle.wechat.service.TagService;
import com.merkle.wechat.service.follower.FollowerService;

@Component
public class DigikeyServiceImpl implements DigikeyService {
    private @Autowired DigikeyLikeOrUnlikeRecordDao digikeyLikeOrUnlikeRecordDao;
    private @Autowired TagService tagServiceImpl;

    @Override
    public LikeOrUnlikeStatisticsVo saveLikeOrUnlikeRecord(DigikeyLikeOrUnlikeRecord record) throws Exception {
        if (!(record.getVote().equals(DigikeyLikeOrUnlikeRecord.LIKE)
                || record.getVote().equals(DigikeyLikeOrUnlikeRecord.UNLIKE))) {
            throw new ServiceWarn("vote not fit!");
        }

        if (StringUtils.isEmpty(record.getIdentityId()) || StringUtils.isEmpty(record.getPageAlias())) {
            throw new ServiceWarn("identityId or pageAlias cant be empty!");
        }

        DigikeyLikeOrUnlikeRecord dbRecord = digikeyLikeOrUnlikeRecordDao
                .findOneByIdentityIdAndPageAlias(record.getIdentityId(), record.getPageAlias());

        if (dbRecord == null) {
            dbRecord = digikeyLikeOrUnlikeRecordDao.save(record);
        } else if (!dbRecord.getVote().equals(record.getVote())) {
            dbRecord.setVote(record.getVote());
            dbRecord.setUpdated(new Date());
            dbRecord.setHost(record.getHost());
            dbRecord.setUserAgent(record.getUserAgent());
            dbRecord = digikeyLikeOrUnlikeRecordDao.save(dbRecord);
        }

        return generateLikeOrUnlikeStatisticsVo(dbRecord);
    }

    @Override
    public LikeOrUnlikeStatisticsVo getLikeInfo(String identityId, String pageAlias) {
        DigikeyLikeOrUnlikeRecord dbRecord = Optional
                .ofNullable(digikeyLikeOrUnlikeRecordDao.findOneByIdentityIdAndPageAlias(identityId, pageAlias))
                .orElseGet(() -> new DigikeyLikeOrUnlikeRecord());
        dbRecord.setIdentityId(identityId);
        dbRecord.setPageAlias(pageAlias);

        return generateLikeOrUnlikeStatisticsVo(dbRecord);
    }

    private LikeOrUnlikeStatisticsVo generateLikeOrUnlikeStatisticsVo(DigikeyLikeOrUnlikeRecord dbRecord) {
        long totalLike = digikeyLikeOrUnlikeRecordDao.countByPageAliasAndVote(dbRecord.getPageAlias(),
                DigikeyLikeOrUnlikeRecord.LIKE);
        long totalUnlike = digikeyLikeOrUnlikeRecordDao.countByPageAliasAndVote(dbRecord.getPageAlias(),
                DigikeyLikeOrUnlikeRecord.UNLIKE);
        LikeOrUnlikeStatisticsVo vo = new LikeOrUnlikeStatisticsVo();
        vo.setIdentityId(dbRecord.getIdentityId());
        vo.setPageAlias(dbRecord.getPageAlias());
        vo.setTotalLike(totalLike);
        vo.setTotalUnlike(totalUnlike);
        vo.setVote(dbRecord.getVote());
        return vo;
    }

    private @Autowired FollowerService followerServiceImpl;

    @Override
    public void tagVipForDigikey(String openid, WechatPublicNo pbNo) {
        Tag tag = tagServiceImpl.getTagByName("digikeyVipFollower", pbNo);
        if (tag != null) {
            Set<Tag> tags = new HashSet<>();
            tags.add(tag);
            followerServiceImpl.tagFollower(openid, tags, pbNo.getAuthorizerAppid());
        }
    }

}
