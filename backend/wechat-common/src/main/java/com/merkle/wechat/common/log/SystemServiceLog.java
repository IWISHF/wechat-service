package com.merkle.wechat.common.log;

import java.util.Map;
/**
 * Ï
 * @author tyao
 *
 */
public class SystemServiceLog {
    private static final String SERVICE_LOG_TYPE_FLAG = "service";

    public SystemServiceLog() {
        super();
        this.type = SERVICE_LOG_TYPE_FLAG;
    }

    private String type;
    private String level;
    private String category;
    private String message;
    private Map<String, Object> context;
    private String backtrace;
    private String reqId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public String getBacktrace() {
        return backtrace;
    }

    public void setBacktrace(String backtrace) {
        this.backtrace = backtrace;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }
}