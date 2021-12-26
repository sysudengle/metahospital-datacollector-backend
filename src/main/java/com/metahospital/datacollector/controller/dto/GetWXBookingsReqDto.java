package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class GetWXBookingsReqDto extends BaseDto {
    private int profileId; // 根据档案id获取所有预约
}
