package com.merkle.wechat.common.entity.digikey.wish;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.merkle.wechat.common.entity.BaseEntity;

@Entity(name = "digikey_wish_share_lock")
public class ShareLock extends BaseEntity {
    @Column(name = "lock_str")
    private String lock;

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

}
