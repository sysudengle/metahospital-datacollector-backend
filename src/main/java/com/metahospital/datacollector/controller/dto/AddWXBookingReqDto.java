package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class AddWXBookingReqDto extends BaseDto {
    private long profileId; // 档案id
    private BookingInfoDto bookingInfoDto;
}
