package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.DoctorStatus;
import lombok.Data;

@Data
public class GetWXDoctorRspDto {
    private boolean exists;
    private DoctorStatus doctorStatus;
    private int hospitalId;
    private String hospitalName;
}
