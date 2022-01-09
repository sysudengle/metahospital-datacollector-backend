package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class GetWXBookingsReqDto extends BaseDto {
    private long profileId; // 根据档案id获取所有预约
    private int hospitalId; // 数据库的主键需要hospitalId
}
