package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.DoctorStatus;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class DoctorInfoDto {
    private HospitalDto hospital;
    private DoctorStatus status = DoctorStatus.Unknown;
    private List<DoctorPermissionDto> permissions = Collections.emptyList(); // 科室权限类型，全科室/外科/口腔科...
}
