package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class ItemValueDto {
    /** 指标项Id */
    private int itemId;
    /** 指标项数值 */
    private String value;
}
