package com.merkle.wechat.vo.follower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.merkle.wechat.common.entity.Tag;
import com.merkle.wechat.common.entity.follower.Follower;

public class FollowerTagResult {
    private Tag tag;
    private Integer total;
    private Integer filtered;
    private List<Follower> success = new ArrayList<Follower>();
    private Map<String, List<Follower>> failed = new HashMap<>();

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFiltered() {
        return filtered;
    }

    public void setFiltered(Integer filtered) {
        this.filtered = filtered;
    }

    public List<Follower> getSuccess() {
        return success;
    }

    public void setSuccess(List<Follower> success) {
        this.success = success;
    }

    public Map<String, List<Follower>> getFailed() {
        return failed;
    }

    public void setFailed(Map<String, List<Follower>> failed) {
        this.failed = failed;
    }

}
