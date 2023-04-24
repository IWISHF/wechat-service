package com.merkle.wechat.vo.follower;

public class FollowerCheckVo {
    private boolean checkFollower = true;
    private boolean checkMember = true;
    private boolean checkSurvey;
    private Long surveyId;
    private boolean checkCampaign;
    private Long campaignId;

    public boolean isCheckFollower() {
        return checkFollower;
    }

    public void setCheckFollower(boolean checkFollower) {
        this.checkFollower = checkFollower;
    }

    public boolean isCheckMember() {
        return checkMember;
    }

    public void setCheckMember(boolean checkMember) {
        this.checkMember = checkMember;
    }

    public boolean isCheckSurvey() {
        return checkSurvey;
    }

    public void setCheckSurvey(boolean checkSurvey) {
        this.checkSurvey = checkSurvey;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public boolean isCheckCampaign() {
        return checkCampaign;
    }

    public void setCheckCampaign(boolean checkCampaign) {
        this.checkCampaign = checkCampaign;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

}
