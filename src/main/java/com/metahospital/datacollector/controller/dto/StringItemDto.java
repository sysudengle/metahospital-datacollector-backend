package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.ItemType;
import lombok.Data;

/**
 * 字符类型指标
 */
@Data
public class StringItemDto extends BaseItemDto {
    /** 字符指标类型Id */
    private int stringItemTypeId;
    /** 默认值 */
    private String defaultValue;
    
    public StringItemDto() {
        this.itemType = ItemType.StringType;
    }
}
