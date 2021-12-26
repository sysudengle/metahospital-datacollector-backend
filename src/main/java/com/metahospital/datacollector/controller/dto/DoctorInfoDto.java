package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.DepartmentPermission;

public class DoctorInfoDto {
    private long hospitalId = -1; // 医院id
    private Boolean isValidDoctor = false; // 医生权限是否已生效
    private DepartmentPermission permission = DepartmentPermission.ALL; // 科室权限类型，全科室/外科/口腔科...
}
