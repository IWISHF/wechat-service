package com.merkle.wechat;

import java.util.Date;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.merkle.wechat.common.dao.RewardsRedeemLogDao;
import com.merkle.wechat.common.dao.follower.FollowerDao;
import com.merkle.wechat.common.entity.RewardsRedeemLog;
import com.merkle.wechat.common.entity.follower.Follower;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTest {
    private @Autowired RewardsRedeemLogDao rewardsRedeemLogDaoImpl;
    private @Autowired FollowerDao followerDaoImpl;
    // @Test
    // @Transactional
    // public void testSaveTagGroup() {
    // TagGroup g = new TagGroup();
    // g.setName("testSave");
    // g.setWechatPublicNoId(2L);
    // g = tagGroupDaoImpl.save(g);
    // System.out.println("save success " + g.getId());
    // }
//
//    @Test
//    public void testSpecialCharacter() {
//        RewardsRedeemLog log = new RewardsRedeemLog();
//        log.setRewardId("1");
//        log.setRewardName("reward name");
//        log.setName("👻Accumulator👻💤💤💤");
//        System.out.println("count success " + rewardsRedeemLogDaoImpl.save(log));
//    }

    @Test
    public void testDistinctOpenid() {
        Follower f = new Follower();
        Date before15Day = new Date(System.currentTimeMillis() - 15 * 24 * 60 * 60 * 1000L);
        Set<String> existsOpenids = followerDaoImpl.getDistinctOpenIdsByAppIdAndUpdatedDate("wx50f190ca3a0ee011", before15Day);
        System.out.println(existsOpenids);
    }

}
