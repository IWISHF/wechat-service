package com.merkle.wechat.vo.follower;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import com.merkle.wechat.common.entity.Tag;

public class FollowerTagVo {
    @Size(min = 1)
    private List<Long> followerIds;
    @Size(min = 1)
    private Set<Tag> tags;

    public List<Long> getFollowerIds() {
        return followerIds;
    }

    public void setFollowerIds(List<Long> followerIds) {
        this.followerIds = followerIds;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

}
