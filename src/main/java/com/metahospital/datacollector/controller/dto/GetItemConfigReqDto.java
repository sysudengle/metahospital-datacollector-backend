package com.metahospital.datacollector.controller.dto;


import lombok.Data;

import java.util.List;

@Data
public class GetItemConfigReqDto extends BaseDto {
    /** 查询指标项Id列表，不能为空 */
    private List<Integer> itemIds;
}
