package com.metahospital.datacollector.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpsertWXItemReqDto extends BaseDto {
    private long bookingId;
    private int departmentId; // 用于校验医生科室权限
    private List<WriteItemDto> itemDtos;
}
