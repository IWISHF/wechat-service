package com.merkle.wechat.security;

/**
 * 
 * @author tyao
 *
 */
public class UserInfo {
    private String userId;
    private String pbNoIds;
    private String role;

    public UserInfo() {
    }

    public UserInfo(String userId, String pbNoIds, String role) {
        this.userId = userId;
        this.pbNoIds = pbNoIds;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPbNoIds() {
        return pbNoIds;
    }

    public void setPbNoIds(String pbNoIds) {
        this.pbNoIds = pbNoIds;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
