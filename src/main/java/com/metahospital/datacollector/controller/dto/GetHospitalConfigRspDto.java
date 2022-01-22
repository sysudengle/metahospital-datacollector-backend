package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.config.configs.HospitalConfigData;
import lombok.Data;

import java.util.List;

@Data
public class GetHospitalConfigRspDto {
    /** todo why == 应该要使用dto数据结构 */
    private List<HospitalConfigData> hospitalConfigDataList;
}
