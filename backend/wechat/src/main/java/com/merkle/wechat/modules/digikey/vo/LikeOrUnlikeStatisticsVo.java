package com.merkle.wechat.modules.digikey.vo;

public class LikeOrUnlikeStatisticsVo {
    private long totalLike;
    private long totalUnlike;
    private String vote;
    private String identityId;
    private String pageAlias;

    public long getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(long totalLike) {
        this.totalLike = totalLike;
    }

    public long getTotalUnlike() {
        return totalUnlike;
    }

    public void setTotalUnlike(long totalUnlike) {
        this.totalUnlike = totalUnlike;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getPageAlias() {
        return pageAlias;
    }

    public void setPageAlias(String pageAlias) {
        this.pageAlias = pageAlias;
    }

}
