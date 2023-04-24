package com.merkle.wechat.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "channel_bind_config")
public class ChannelBindConfig extends BaseEntity {
    @Column(name = "arrow_start")
    private Long from;
    @Column(name = "arrow_end")
    private Long to;

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

}
