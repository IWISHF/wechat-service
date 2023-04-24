package com.merkle.wechat.vo.follower;

import com.merkle.wechat.common.entity.follower.Follower;
import com.merkle.wechat.common.entity.follower.FollowerBindInfo;

public class FollowerRelatedVo {
    private boolean followed;
    private boolean enrolled;
    private Follower follower;
    private FollowerBindInfo member;

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    public Follower getFollower() {
        return follower;
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }

    public FollowerBindInfo getMember() {
        return member;
    }

    public void setMember(FollowerBindInfo member) {
        this.member = member;
    }
}
