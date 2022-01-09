package com.metahospital.datacollector.controller.dto;

import lombok.Data;

// 预约号bookingId可以确认对应什么combo，进而确认哪些指标项需要测试，再通过departmentId过滤得出需要展示的item
@Data
public class GetWXItemsReqDto extends BaseDto {
    private long bookingId;
    private int departmentId;
}
