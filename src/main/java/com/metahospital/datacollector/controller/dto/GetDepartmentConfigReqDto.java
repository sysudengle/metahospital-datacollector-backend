package com.metahospital.datacollector.controller.dto;


import lombok.Data;

import java.util.List;

@Data
public class GetDepartmentConfigReqDto extends BaseDto {
    /** 查询科室Id列表，不能为空 */
    private List<Integer> departmentIds;
}
