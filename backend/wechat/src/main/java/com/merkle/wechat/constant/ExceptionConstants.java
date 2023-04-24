package com.merkle.wechat.constant;

public class ExceptionConstants {
    // ERROR CODE
    public static final int UN_KNOWN_ERROR = -1;
    public static final int PARAM_ERROR = 1001;
    public static final int TIME_OUT_ERROR = 1002;
    public static final int RESULT_NOT_MATCH_ERROR = 1003;
    public static final int WECONNECT_SERVER_ERROR = 1004;
    public static final int FOLLOWER_NOT_EXIST = 1005;
    public static final int ALREADY_EXIST = 1006;
    public static final int NOT_EXIST = 1007;
    public static final int TOKEN_NOT_EXIST = 1008;
    public static final int NOT_EMPTY = 1009;
    public static final int PASSWORD_ERROR = 1010;
    public static final int MISSING_PARAM_ERROR = 1011;
    public static final int REACH_LIMITATION = 1012;
    public static final int LOGIN_LOCK = 28001;
    // WechatPublic no
    public static final int WECHAT_PUBLIC_NO_NOT_EXIST = 21001;

    // Article
    public static final int ARTICLE_CANT_DELETE = 22001;

    // Keyword auto reply
    public static final int AUTOREPLY_CANT_BE_EMPTY = 23001;

    // tag
    public static final int DEFAULT_GROUP_CANT_DELETE = 24001;
    public static final int DEFAULT_GROUP_CANT_EDIT = 24002;

    // qrcode
    public static final int NAME_ALREADY_EXIST = 25001;

    // batch task
    public static final int BATCH_TASK_CANT_DELETE = 26001;
    public static final int BATCH_TASK_CREATE_FOLLOWER_COUNT_ZERO_ERROR = 26002;
    public static final int BATCH_TASK_CREATE_TRIGGER_DATE_NOT_FUTURE_ERROR = 26003;

    // Captcha
    public static final int CAPTCHA_ERROR = 27001;
    public static final int CAPTCHA_MAX_TIMES_LIMIT = 27002;
    public static final int CAPTCHA_SIXTY_SECONDS_LIMIT = 27003;
    public static final int CREDENTIAL_NOT_MATCH_ERROR = 27004;
    public static final int CAPTCHA_EXPIRED_ERROR = 27005;
    public static final int CAPTCHA_ALREADY_USED_ERROR = 27006;

    public static final int CAPTCHA_SP_API_ERROR = 27100;
    public static final int CAPTCHA_MLINK_INTERNAL_ROUTE_FILTER_ERROR = 27200;
    public static final int CAPTCHA_MLINK_INTERNAL_SP_CONFIG_ERROR = 27300;
    public static final int CAPTCHA_MLINK_SEND_ERROR = 27400;
    public static final int CAPTCHA_OPERATER_ERROR = 27500;
    public static final int CAPTCHA_MLINK_API_SEND_ERROR = 27600;

    // Campaign
    public static final int CAMPAIGN_CANT_ACCESS_NOW = 29000;
    public static final int CAMPAIGN_OFFLINE_CHECKIN_ERROR = 29001;

    // RewardsRedeemLog
    public static final int ALREADY_USED = 30000;

    // Survey
    public static final int SURVEY_CANT_ACCESS_NOW = 31000;


    // param exception msg
    public static final String BODY_PARAM_NOT_EXIST_MSG = "No Body Param!";
    public static final String OPENID_NOT_EXIST_MSG = "OpenId Can't be empty!";

}
