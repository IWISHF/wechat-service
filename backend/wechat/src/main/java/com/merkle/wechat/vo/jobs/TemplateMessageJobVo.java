package com.merkle.wechat.vo.jobs;

import java.util.ArrayList;

import com.merkle.wechat.common.entity.jobs.BaseJob;

public class TemplateMessageJobVo {
    private ArrayList<String> commands;
    private BaseJob baseJob;

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }

    public BaseJob getBaseJob() {
        return baseJob;
    }

    public void setBaseJob(BaseJob baseJob) {
        this.baseJob = baseJob;
    }
}
