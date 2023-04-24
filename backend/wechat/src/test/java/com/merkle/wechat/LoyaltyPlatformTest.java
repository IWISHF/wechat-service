package com.merkle.wechat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.merkle.loyalty.PrismEnv;
import com.merkle.loyalty.apis.EventsAPI;
import com.merkle.loyalty.response.PrismResponse;

import junit.framework.TestCase;

public class LoyaltyPlatformTest extends TestCase {
    PrismEnv env = new PrismEnv();
    {
        env.setBaseUrl("https://lp-cn2-staging.merklechina.com/");
        env.setSecret("edfe4a5ef7ee2a4f19fd8c1c525168ba");
        env.setUuid("fecc0c85571689");
    }

    PrismEnv testEnv = new PrismEnv();
    {
        testEnv.setBaseUrl("https://loyalty-cn-stage.merklechina.com/");
        testEnv.setSecret("22123dd76e80f6b908c8e131933f1a61");
        testEnv.setUuid("93ef385e0bcc1e");
    }

    PrismEnv nbaStageEnv = new PrismEnv();
    {
        nbaStageEnv.setBaseUrl("https://loyalty-cn-stage.merklechina.com/");
        nbaStageEnv.setSecret("IfogMX1aTpIsDAeeedQLk3DoSiLRyfTM");
        nbaStageEnv.setUuid("fgDkfrTura");
    }

    PrismEnv localEnv = new PrismEnv();
    {
        localEnv.setBaseUrl("http://localhost:3000/");
        localEnv.setSecret("IfogMX1aTpIsDAeeedQLk3DoSiLRyfTM");
        localEnv.setUuid("fgDkfrTura");
    }

    PrismEnv nbaProdEnv = new PrismEnv();
    {
        nbaProdEnv.setBaseUrl("https://loyalty-cn.merklechina.com/");
        nbaProdEnv.setSecret("pl2a88mRr6fznQ1eAnP1mTdRzeHOc97H");
        nbaProdEnv.setUuid("KvHnPtd1js");
    }

    // @Test
    // public void testEnrollOrUpdate() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("external_customer_id", "test_env_uuuuuid");
    // params.put("new_vendor", "test_env");
    // params.put("new_vendor_id", "test_env_id");
    // params.put("channel", "WeChat");
    // params.put("status", "active");
    // params.put("name", "test spcace encode");
    //
    // PrismResponse response = CustomerAPI.enrollOrUpdate(env, params);
    // System.out.println(response);
    // }

    // @Test
    // public void testEnroll() throws Exception {
    // String id = "18662657036";
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("email", "test2@test.com");
    // params.put("vendor", "tx_openid");
    // params.put("vendor_id", "openidtest");
    // params.put("channel", "test");
    // params.put("auto_enroll", "true");
    // params.put("name", "JimmyLiao");
    // params.put("identities[0][tx_unionid]", "unionIdtest");
    // // params.put("email", "aa@aae.com");
    // // params.put("custom_attributes[subscribe_status]", "true");
    //
    // PrismResponse response = CustomerAPI.enroll(localEnv, params);
    // System.out.println("++++++++++1==" + response);
    // }

    // @Test
    // public void testUpdate() throws Exception {
    // String id = "on6IF1NL0nZLoXWtoRK32dq_w37w";
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("external_customer_id", id);
    // params.put("new_email", "1233@qq.com");
    // params.put("address_line_1", null);
    // params.put("mobile_phone", "15252323382");
    // params.put("state", "上海");
    // params.put("city", "上海");
    // params.put("first_name", "test");
    // params.put("last_name", "DannyWang");
    // params.put("address_line_2", "浦东新区");
    // PrismResponse response = CustomerAPI.customerUpdateInfo(testEnv, params);
    // System.out.println("++++++++++++++++ part1" + response);
    // params.clear();
    // params.put("external_customer_id", id);
    // params.put("custom_attributes[digikey_customer_number]", "7823jdjfsdy");
    // params.put("custom_attributes[currency]", "人民币");
    // params.put("custom_attributes[career_title]", "Enginner");
    //
    // response = CustomerAPI.customerUpdateInfo(testEnv, params);
    // System.out.println("++++++++++++++++ part2" + response);
    //
    // }

    // @Test
    // public void testRecordMenuClick() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("type", "wechat-menu");
    // params.put("external_customer_id", "uunniioonniidd");
    // params.put("value", "0");
    // params.put("channel", "WeChat");
    //
    // PrismResponse response = EventsAPI.record(env, params);
    // System.out.println(response);
    // }
    //
//    @Test
//    public void testRecordSubscribe() throws Exception {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("type", "mk_expire_coupon");
//        params.put("channel", "mkpos");
//        params.put("value", "30");
//        params.put("detail", "过期记录301");
//        params.put("expiration_date","2019-11-08 00:00:00");
//        params.put("external_customer_id", "18623458651");
////        params.put("detail", "aappppiidd");
////        params.put("channel", "WeChat");
//
//        PrismResponse response = EventsAPI.record(nbaStageEnv, params);
//        System.out.println(response);
//    }
    //
    // @Test
    // public void testRecordPurchase() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("type", "purchase");
    // params.put("external_customer_id", "uunniioonniidd");
    // params.put("value", "116.9");
    // params.put("event_id", "ORDER_987333");
    // params.put("primary_reference_id", "SKU_1288938733");
    // params.put("channel", "WeChat");
    //
    // PrismResponse response = EventsAPI.record(env, params);
    // System.out.println(response);
    // }
    //
    // @Test
    // public void testTextConfirmationCode() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("phone", "17682309714");
    // PrismResponse response = CustomerAPI.textConfirmationCode(testEnv,
    // params);
    // System.out.println("++++ test text confirmationCode ++" + response);
    // }

    // @Test
    // public void testEnrollOrShowCustomer() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("external_customer_id", "17682309714");
    // params.put("address_line_1", "address");
    // params.put("channel", "nba_taobao_h5");
    // params.put("confirmation_code", "9109");
    // params.put("status", "active");
    // params.put("enroll_status", "true");
    // params.put("name", "TerryYao");
    // params.put("vendor", "TAOBAO");
    // params.put("vendor_id", "taobao_eddy");
    // PrismResponse response =
    // CustomerAPI.customerEnrollOrShowCustomer(testEnv, params);
    // System.out.println("++++ test Enroll or show Customer ++" + response);
    // }

    // @Test
    // public void testEnroll2016() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("external_customer_id", "17682309714");
    // params.put("channel", "xiaoheiyu2");
    // // params.put("confirmation_code", "9109");
    // // params.put("password","Test123_");
    // // params.put("password_confirmation","Test123_");
    // params.put("status", "active");
    // params.put("enroll_status", "true");
    // params.put("name", "TerryYao");
    // params.put("vendor", "xiaoheiyu2");
    // params.put("vendor_id", "xiaoheiyu12");
    // params.put("auto_enroll", "true");
    // PrismResponse response = CustomerAPI.textEnrollOrActivateFor2018(testEnv,
    // params);
    // System.out.println("++++ test Enroll or show Customer ++" + response);
    // }

    // @Test
    // public void testUpdate() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("external_customer_id", "12751645390");
    // params.put("custom_attributes[r_name]", "test2");
    // params.put("custom_attributes[r_province]", "上海");
    // params.put("custom_attributes[r_city]", "上海");
    // params.put("custom_attributes[r_district]", "浦东新区");
    // params.put("custom_attributes[r_address]", "浦东新区孤傲路梅花村200弄好好");
    // PrismResponse response = CustomerAPI.customerUpdateInfo(nbaStageEnv,
    // params);
    // System.out.println("++++ test update Customer ++" + response);
    // }

    // @Test
    // public void testRedeem() throws Exception {
    // FollowerBindInfo bindInfo = new FollowerBindInfo();
    // bindInfo.setAddress("测试地址");
    // bindInfo.setAppId("appId");
    // bindInfo.setEmail("yaosingyao@gmail.com");
    // bindInfo.setName("Terry");
    // bindInfo.setOpenid("otJq0w3MfYi3KUW8X2wAH6eBs34s");
    // bindInfo.setPhone("18662657036");
    // bindInfo.setQq("123121231432");
    // bindInfo.setTitle("工程师");
    // bindInfo.setWechatPublicNoId(2L);
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("external_customer_id", bindInfo.getOpenid());
    // params.put("new_email", bindInfo.getEmail());
    // params.put("mobile_phone", bindInfo.getPhone());
    // params.put("address_line_1", bindInfo.getAddress());
    // params.put("state", bindInfo.getProvince());
    // params.put("city", bindInfo.getCity());
    // params.put("first_name", bindInfo.getName());
    // PrismResponse response = CustomerAPI.customerUpdateInfo(env, params);
    // System.out.println("++++ sync bind info part1 ++" + response);
    //
    // params.clear();
    // params.put("external_customer_id", bindInfo.getOpenid());
    // params.put("address_line_2", bindInfo.getDistrict());
    // params.put("custom_attributes[digikey_customer_number]",
    // bindInfo.getDigikeyCustomerNumber());
    // params.put("custom_attributes[currency]", bindInfo.getCurrency());
    // params.put("custom_attributes[career_title]", bindInfo.getTitle());
    // params.put("custom_attributes[tencent_qq]", "1");
    //
    // response = CustomerAPI.customerUpdateInfo(env, params);
    // System.out.println("++++ sync bind info part2 ++" + response);
    // }
    //
//    @Test
//    public void testCustomerInfo() throws Exception {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("external_customer_id", "ct_0495d370-1473-4123-8400-4b22e3704261");
//        //params.put("include", "badges,coupons,offers,rewards,member_attributes,events,reward_stats");
//
//        PrismResponse response = CustomerAPI.customerShow(nbaProdEnv, params);
//        System.out.println(response);
//    }
    //
//    @Test
//    public void testCustomerEvents() throws Exception {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("external_customer_id", "18623458651");
//        params.put("date_filter", "created_at");
//        params.put("after_date", "20191112");
////        params.put("page_number", "1");
////        params.put("entries_per_page", "10");
//
//        PrismResponse response = CustomerAPI.customerEvents(nbaStageEnv, params);
//        System.out.println(response);
//    }
    //
    // @Test
    // public void testRewardRedeem() throws Exception {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("external_customer_id", "uunniioonniidd");
    // params.put("reward_id", "115");
    //
    // PrismResponse response = EventsAPI.redeemReward(env, params);
    // System.out.println(response);
    // }
    
    
//    @Test
//    public void testRewardRedeem() throws Exception {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("external_customer_id", "uunniioonniidd");
//        params.put("reward_id", "115");
//
//        PrismResponse response = EventsAPI.redeemReward(env, params);
//        System.out.println(response);
//    }
}
