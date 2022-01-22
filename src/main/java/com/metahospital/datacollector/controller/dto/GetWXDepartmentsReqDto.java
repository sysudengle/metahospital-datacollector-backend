package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class GetWXDepartmentsReqDto extends BaseDto {
    private int hospitalId;
    private long profileId;
    private long bookingId;
}
