package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.ItemType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 选择列表指标类型
 */
@Data
public class SelectionItemDto extends BaseItemDto {
    /** 选择列表指标类型Id */
    private int selectionItemTypeId;
    /** 指标值对应展示名称，如指标值有4个选项对应值0、1、2，则对应展示值为高血糖、中血糖、低血糖 */
    private Map<Integer, String> selectionMap;
    /** 默认选项 */
    private List<Integer> defaultValue;
    /** 可选数量，0表示不定项选，1表示单选 */
    private int selectNum;
    
    public SelectionItemDto() {
        this.itemType = ItemType.SelectionType;
    }
}
