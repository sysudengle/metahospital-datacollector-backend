package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.ItemType;
import lombok.Data;

import java.util.Map;

/**
 * 数值指标类型
 */
@Data
public class NumberItemDto extends BaseItemDto {
    /** 数值指标类型Id */
    private int numberItemTypeId;
    /** 最小值 */
    private long min;
    /** 最大值 */
    private long max;
    /** 小数位数，显示值=实际值/(10^小数位数) */
    private int unitDecimal;
    /** 数值指标单位，如身高对应cm */
    private String unitDesc;
    /** 默认值 */
    private long defaultValue;
    
    public NumberItemDto() {
        this.itemType = ItemType.NumberType;
    }
}
