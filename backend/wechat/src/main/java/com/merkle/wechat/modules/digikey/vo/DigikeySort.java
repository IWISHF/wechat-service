package com.merkle.wechat.modules.digikey.vo;

import io.swagger.annotations.ApiModelProperty;

public class DigikeySort {

    @ApiModelProperty(required = true, value = "default SortByCategory", allowableValues = "SortByDigiKeyPartNumber,SortByManufacturerPartNumber, SortByDescription,SortByCategory,SortByFamily,SortByManufacturer,SortByMinimumOrderQuantity,SortByQuantityAvailable,SortByUnitPrice,SortByParameter")
    private String option = "SortByCategory";
    @ApiModelProperty(required = true, value = "default Ascending", allowableValues = "Ascending, Descending")
    private String direction = "Ascending";
    @ApiModelProperty(required = true, value = "The ParameterId of the parameter to sort on. Only used if Option is SortByParameter, but is required. If using other Options, input 0")
    private int sortParameterId = 0;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSortParameterId() {
        return sortParameterId;
    }

    public void setSortParameterId(int sortParameterId) {
        this.sortParameterId = sortParameterId;
    }

}
