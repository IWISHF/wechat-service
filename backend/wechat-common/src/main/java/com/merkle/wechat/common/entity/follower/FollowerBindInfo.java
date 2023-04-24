package com.merkle.wechat.common.entity.follower;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Email;

import com.merkle.wechat.common.annotation.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "follower_bind_info")
public class FollowerBindInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "创建不必须")
    private Long id;
    @NotEmpty
    @ApiModelProperty(value = "创建必须", required = true)
    private String openid;
    private String unionid;
    @ApiModelProperty(value = "创建不必须")
    private String appId;
    @NotNull
    @ApiModelProperty(value = "创建必须", required = true)
    private Long wechatPublicNoId;
    @NotEmpty
    private String phone;
    @Email
    private String email;
    private String qq;
    private String name;
    private String title;
    private String currency;
    private String digikeyCustomerNumber;
    private String country;
    private String province;
    private String city;
    private String district;
    private String address;
    private boolean syncToLoyalty = false;
    private Date createdDate = new Date();
    private Date updatedDate = new Date();
    private String fixFrom;

    private final static String[] ENGINEER_STUDENT = { "工程师", "学生" };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getWechatPublicNoId() {
        return wechatPublicNoId;
    }

    public void setWechatPublicNoId(Long wechatPublicNoId) {
        this.wechatPublicNoId = wechatPublicNoId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDigikeyCustomerNumber() {
        return digikeyCustomerNumber;
    }

    public void setDigikeyCustomerNumber(String digikeyCustomerNumber) {
        this.digikeyCustomerNumber = digikeyCustomerNumber;
    }

    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSyncToLoyalty() {
        return syncToLoyalty;
    }

    public void setSyncToLoyalty(boolean syncToLoyalty) {
        this.syncToLoyalty = syncToLoyalty;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getFixFrom() {
        return fixFrom;
    }

    public void setFixFrom(String fixFrom) {
        this.fixFrom = fixFrom;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public static Boolean isEnginnerOrStudent(FollowerBindInfo member) {
        return ArrayUtils.contains(ENGINEER_STUDENT, member.getTitle());
    }
}
