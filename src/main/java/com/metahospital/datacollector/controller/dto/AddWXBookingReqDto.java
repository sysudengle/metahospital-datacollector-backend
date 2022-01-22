package com.metahospital.datacollector.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddWXBookingReqDto extends BaseDto {
    /** 医院id */
    private int hospitalId;
    /** 档案id */
    private long profileId;
    /** 预约信息 */
    private BookingInfoDto bookingInfoDto;
}
