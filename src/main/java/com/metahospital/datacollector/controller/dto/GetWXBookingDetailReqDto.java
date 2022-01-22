package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class GetWXBookingDetailReqDto extends BaseDto {
    /** 医院Id */
    private int hospitalId;
    /** 档案id */
    private long profileId;
    /** 预约id */
    private long bookingId;
}
