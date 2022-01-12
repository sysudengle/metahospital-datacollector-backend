package com.metahospital.datacollector.controller.dto;


import lombok.Data;

@Data
public class GetBookingConfigReqDto {
    private long bookingId = -1; // 写操作自行生成
}
