package com.merkle.wechat.modules.digikey.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DigikeyProductDetailVo {
    @ApiModelProperty(required = true)
    private String part;
    private String includeAllAssociateProducts = "false";
    private String includeAllForUseWithProducts = "false";
    private String currency = "CNY";

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getIncludeAllAssociateProducts() {
        return includeAllAssociateProducts;
    }

    public void setIncludeAllAssociateProducts(String includeAllAssociateProducts) {
        this.includeAllAssociateProducts = includeAllAssociateProducts;
    }

    public String getIncludeAllForUseWithProducts() {
        return includeAllForUseWithProducts;
    }

    public void setIncludeAllForUseWithProducts(String includeAllForUseWithProducts) {
        this.includeAllForUseWithProducts = includeAllForUseWithProducts;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    
}
