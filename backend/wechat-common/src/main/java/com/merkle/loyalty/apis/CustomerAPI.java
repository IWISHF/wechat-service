package com.merkle.loyalty.apis;

import java.util.Map;

import com.merkle.loyalty.PrismEnv;
import com.merkle.loyalty.config.PathConfig;
import com.merkle.loyalty.response.PrismResponse;

/**
 * 
 * @author tyao
 *
 */
public class CustomerAPI extends BaseAPI {

    public static PrismResponse enrollOrUpdate(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.ENROLL_OR_UPDATE, params);
    }

    public static PrismResponse enroll(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.ENROLL, params);
    }

    public static PrismResponse customerEvents(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.CUSTOMER_EVENTS, params);
    }

    public static PrismResponse customerShow2016(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.CUSTOMER_SHOW_2016, params);
    }

    public static PrismResponse customerShow(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.CUSTOMER_SHOW, params);
    }

    public static PrismResponse customerRewards(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.CUSTOMER_REWARDS, params);
    }

    public static PrismResponse customerUpdateAttributes(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.CUSTOMER_UPDATE_ATTRIBUTES, params);
    }
    
    public static PrismResponse customerUpdateInfo(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.CUSTOMER_UPDATE_INFO, params);
    }

    public static PrismResponse customerCoupons(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, PathConfig.CUSTOMER_COUPONS, params);
    }

    public static PrismResponse customerEnrollOrShowCustomer(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, "2018-01-01/api/enroll_or_show_customer.json", params);
    }
    
    public static PrismResponse textConfirmationCode(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, "api/text_confirmation", params);
    }
    
    public static PrismResponse textEnrollFor2016(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, "2016-07-01/api/enroll.json", params);
    }
    
    public static PrismResponse textEnrollOrActivateFor2018(PrismEnv env, Map<String, String> params) throws Exception {
        return send(env, "2018-12-01/api/enroll_or_activate.json", params);
    }
}