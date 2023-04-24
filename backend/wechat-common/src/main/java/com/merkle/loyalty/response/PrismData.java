package com.merkle.loyalty.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrismData {
	private int id;
	private String channel;
	private String created_at;
	private String email;
	private String enrolled_at;
	private String external_customer_id;
	private String last_activity;
	private String last_reward_date;
	private String last_reward_event_id;
	private String status;
	private String sub_channel;
	private String sub_channel_detail;
	private String subscription_type;
	private String unsubscribed;
	private String unsubscribed_sms;
	private String updated_at;
	private String name;
	private String balance;
	private String lifetime_balance;
	private String image_url;
	private String top_tier_name;
	private String top_tier_expiration_date;
	private int code;
	private String message;
	
	private Map<String, Object> dynamicProperties = new HashMap<>();
	
	@JsonAnyGetter
    public Map<String, Object> getDynamicProperties() {
        return this.dynamicProperties;
    }

    @JsonAnySetter
    public void setDynamicProperty(String name, Object value) {
        this.dynamicProperties.put(name, value);
    }
    
    public Object getDynamicProperty(String name) {
    		return this.dynamicProperties.get(name);
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEnrolled_at() {
		return enrolled_at;
	}

	public void setEnrolled_at(String enrolled_at) {
		this.enrolled_at = enrolled_at;
	}

	public String getExternal_customer_id() {
		return external_customer_id;
	}

	public void setExternal_customer_id(String external_customer_id) {
		this.external_customer_id = external_customer_id;
	}

	public String getLast_activity() {
		return last_activity;
	}

	public void setLast_activity(String last_activity) {
		this.last_activity = last_activity;
	}

	public String getLast_reward_date() {
		return last_reward_date;
	}

	public void setLast_reward_date(String last_reward_date) {
		this.last_reward_date = last_reward_date;
	}

	public String getLast_reward_event_id() {
		return last_reward_event_id;
	}

	public void setLast_reward_event_id(String last_reward_event_id) {
		this.last_reward_event_id = last_reward_event_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSub_channel() {
		return sub_channel;
	}

	public void setSub_channel(String sub_channel) {
		this.sub_channel = sub_channel;
	}

	public String getSub_channel_detail() {
		return sub_channel_detail;
	}

	public void setSub_channel_detail(String sub_channel_detail) {
		this.sub_channel_detail = sub_channel_detail;
	}

	public String getSubscription_type() {
		return subscription_type;
	}

	public void setSubscription_type(String subscription_type) {
		this.subscription_type = subscription_type;
	}

	public String getUnsubscribed() {
		return unsubscribed;
	}

	public void setUnsubscribed(String unsubscribed) {
		this.unsubscribed = unsubscribed;
	}

	public String getUnsubscribed_sms() {
		return unsubscribed_sms;
	}

	public void setUnsubscribed_sms(String unsubscribed_sms) {
		this.unsubscribed_sms = unsubscribed_sms;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLifetime_balance() {
		return lifetime_balance;
	}

	public void setLifetime_balance(String lifetime_balance) {
		this.lifetime_balance = lifetime_balance;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getTop_tier_name() {
		return top_tier_name;
	}

	public void setTop_tier_name(String top_tier_name) {
		this.top_tier_name = top_tier_name;
	}

	public String getTop_tier_expiration_date() {
		return top_tier_expiration_date;
	}

	public void setTop_tier_expiration_date(String top_tier_expiration_date) {
		this.top_tier_expiration_date = top_tier_expiration_date;
	}
}
