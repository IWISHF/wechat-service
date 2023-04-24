package com.merkle.wechat.vo.follower;

import javax.validation.constraints.NotNull;

import com.merkle.wechat.common.annotation.NotEmpty;

public class TicketCheckVo {
    @NotEmpty
    private String openid;
    @NotEmpty
    private String rewardId;
    @NotEmpty
    private String code;
    @NotNull
    private Long channelId;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

}
