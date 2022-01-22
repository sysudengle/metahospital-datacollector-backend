package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.DoctorStatus;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class DoctorInfoDto {
    private HospitalDto hospital;
    private DoctorStatus status;
    private List<Integer> departmentIds;
}
