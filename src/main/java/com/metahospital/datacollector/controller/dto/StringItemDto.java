package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.ItemType;
import lombok.Data;

// 字符类型指标
@Data
public class StringItemDto extends BaseItemDto {
    public StringItemDto() {
        this.itemType = ItemType.StringType;
    }

    private String unitName; // 指标单位，如身高对应cm
}
