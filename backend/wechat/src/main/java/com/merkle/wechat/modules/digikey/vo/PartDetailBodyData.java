package com.merkle.wechat.modules.digikey.vo;

public class PartDetailBodyData {
    private String part;
    private String includeAllAssociateProducts = "false";
    private String includeAllForUseWithProducts = "false";

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

}
