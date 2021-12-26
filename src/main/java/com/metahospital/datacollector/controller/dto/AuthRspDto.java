package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.UserType;
import lombok.Data;

@Data
public class AuthRspDto extends BaseDto {
    // TODO 补充额外小程序需要字段
    private UserType uType;
    private DoctorInfoDto doctorInfo; // 如果是医生会包含医生信息

    public AuthRspDto() {
    }

    public AuthRspDto(String openId, String userId) {
        this.setOpenId(openId);
        this.setUserId(userId);
    }
}
