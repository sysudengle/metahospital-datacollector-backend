package com.metahospital.datacollector.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddWXBookingReqDto extends BaseDto {
    private long profileId; // 档案id
    private int hospitalId; //医院id,由于后端主键问题，hospitalId 不加的话调不出数据
    private BookingInfoDto bookingInfoDto;
}
