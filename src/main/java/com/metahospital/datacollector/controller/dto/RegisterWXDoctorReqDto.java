package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class RegisterWXDoctorReqDto extends BaseDto {
    private long hospitalId;
    private String staffId;
}
