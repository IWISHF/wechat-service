package com.merkle.wechat.common.entity.aia;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.merkle.wechat.common.annotation.NotEmpty;

@Entity(name = "aia_intention_data")
public class AIAIntentionData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    @Pattern(regexp = "[0,1]{4}")
    private String consultForWho;// 子女， 自己， 配偶，父母

    @NotEmpty
    @Pattern(regexp = "[0,1]{4}")
    private String type;// 保险类型, 意外 医疗 教育 其他

    @NotEmpty
    @Length(min = 1, max = 12)
    private String name;

    @NotEmpty
    @Pattern(regexp = "[0-9]{11}")
    @Length(min = 11, max = 11)
    private String phone;

    @NotEmpty
    private String fromWhich;

    private Date createdDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsultForWho() {
        return consultForWho;
    }

    public void setConsultForWho(String consultForWho) {
        this.consultForWho = consultForWho;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFromWhich() {
        return fromWhich;
    }

    public void setFromWhich(String fromWhich) {
        this.fromWhich = fromWhich;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
