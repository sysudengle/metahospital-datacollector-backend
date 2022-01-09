package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.ItemType;
import lombok.Data;

import java.util.Map;

// 下拉列表指标类型
@Data
public class SelectionItemDto extends BaseItemDto {
    public SelectionItemDto() {
        this.itemType = ItemType.SelectionType;
    }

    private Map<String, String> selectionMap; // 指标值对应展示名称，如指标值有4个选项对应值0、1、2，则对应展示值为高血糖、中血糖、低血糖
}
