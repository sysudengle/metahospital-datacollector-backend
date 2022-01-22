package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class GetWXBookingsReqDto extends BaseDto {
    /** 医院Id */
    private int hospitalId;
    /** 根据档案id获取所有预约 */
    private long profileId;
}
