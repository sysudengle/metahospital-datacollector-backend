package com.metahospital.datacollector.controller.dto;


import lombok.Data;

import java.util.List;

@Data
public class GetComboConfigReqDto extends BaseDto {
    /** 查询套餐Id列表，不能为空 */
    private List<Integer> comboIds;
}
