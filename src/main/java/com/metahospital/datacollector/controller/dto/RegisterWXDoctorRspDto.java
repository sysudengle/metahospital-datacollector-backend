package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.DoctorStatus;
import com.metahospital.datacollector.common.enums.UserType;
import lombok.Data;

// 保留用于后续添加字段
@Data
public class RegisterWXDoctorRspDto{
    private DoctorStatus doctorStatus;
    private UserType uType;
    private String accountName;
    private String password;
    private String hospitalName;
    private String departmentName;
}
