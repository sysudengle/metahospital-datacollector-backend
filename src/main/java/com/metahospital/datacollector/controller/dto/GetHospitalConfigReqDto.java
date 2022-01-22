package com.metahospital.datacollector.controller.dto;


import lombok.Data;

import java.util.List;

@Data
public class GetHospitalConfigReqDto extends BaseDto {
    /** 查询医院Id列表，为空时表示查询全部 */
    private List<Integer> hospitalIds;
}
