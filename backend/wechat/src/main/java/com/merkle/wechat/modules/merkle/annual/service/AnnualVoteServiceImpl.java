package com.merkle.wechat.modules.merkle.annual.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.merkle.wechat.common.dao.AnnualFlagDao;
import com.merkle.wechat.common.dao.AnnualVoteDao;
import com.merkle.wechat.common.entity.AnnualPartyFlag;
import com.merkle.wechat.common.entity.AnnualVote;
import com.merkle.wechat.modules.merkle.annual.vo.VoteSummaryVo;
import com.merkle.wechat.modules.merkle.annual.vo.VoteVo;

@Component
public class AnnualVoteServiceImpl {
    private String ENABLE = "enable";
    private String DISABLE = "disable";

    private @Autowired AnnualVoteDao voteDao;

    public VoteVo valid(String openid) {
        VoteVo vo = new VoteVo();
        AnnualVote vote = voteDao.findOneByOpenid(openid);
        if (vote == null) {
            vo.setStatus(DISABLE);
            return vo;
        }
        vo.setStatus(ENABLE);
        vo.setVoteStr(vote.getVoteStr());
        return vo;
    }

    public void save(VoteVo vo) {
        AnnualVote vote = voteDao.findOneByOpenid(vo.getOpenid());
        if (vote != null) {
            vote.setUpdated(new Date());
            vote.setVoteStatus(AnnualVote.VALID);
            vote.setVoteStr(vo.getVoteStr());
            voteDao.save(vote);
        }
    }

    public VoteSummaryVo summary() {
        VoteSummaryVo vo = new VoteSummaryVo();
        List<AnnualVote> allVotes = (List<AnnualVote>) voteDao.findAll();
        if (allVotes != null) {
            vo.setTotalParticipation(allVotes.size());
            int validParticipation = 0;
            int totalVotes = 0;
            int[] dtOfVotes = vo.getDistributionOfVotes();
            for (AnnualVote vote : allVotes) {
                if (vote.getVoteStatus().equals(AnnualVote.INIT)) {
                    continue;
                }

                validParticipation++;
                // parse to binary
                Long voteValue = Long.valueOf(vote.getVoteStr(), 2);
                if (voteValue == 0) {
                    continue;
                }

                Long mask = 1L;
                for (int index = VoteSummaryVo.LENGTH - 1; index >= 0; index--) {
                    if ((voteValue >> index & mask) == 1) {
                        dtOfVotes[VoteSummaryVo.LENGTH -1 - index]++;
                        totalVotes++;
                    }
                }
            }

            vo.setDistributionOfVotes(dtOfVotes);
            vo.setTotalVotes(totalVotes);
            vo.setValidParticipation(validParticipation);
        }
        return vo;
    }

    private @Autowired AnnualFlagDao annualFlagDao;

    public void enableOrDisableVote(String type) {
        AnnualPartyFlag flag = annualFlagDao.findOneByType(AnnualPartyFlag.TYPE);
        if (flag == null) {
            flag = new AnnualPartyFlag();
        }

        if (type.equals("enable")) {
            flag.setEnable(true);
        } else if (type.equals("disable")) {
            flag.setEnable(false);
        }
        annualFlagDao.save(flag);
    }

    public boolean enableVote() {
        AnnualPartyFlag flag = annualFlagDao.findOneByType(AnnualPartyFlag.TYPE);
        if (flag != null) {
            return flag.isEnable();
        }
        flag = new AnnualPartyFlag();
        annualFlagDao.save(flag);
        return false;
    }
}
