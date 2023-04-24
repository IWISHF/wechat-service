package com.merkle.wechat.modules.merkle.annual.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VoteSummaryVo {
    @JsonIgnore
    public static final int LENGTH = 10;
    private int totalParticipation;
    private int validParticipation;
    private int totalVotes;
    private int[] distributionOfVotes = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    public int getTotalParticipation() {
        return totalParticipation;
    }

    public void setTotalParticipation(int totalParticipation) {
        this.totalParticipation = totalParticipation;
    }

    public int getValidParticipation() {
        return validParticipation;
    }

    public void setValidParticipation(int validParticipation) {
        this.validParticipation = validParticipation;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public int[] getDistributionOfVotes() {
        return distributionOfVotes;
    }

    public void setDistributionOfVotes(int[] distributionOfVotes) {
        this.distributionOfVotes = distributionOfVotes;
    }

}
