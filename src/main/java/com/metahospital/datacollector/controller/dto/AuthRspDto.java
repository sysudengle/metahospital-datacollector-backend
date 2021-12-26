package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.DepartmentPermission;
import com.metahospital.datacollector.common.UserType;
import lombok.Data;

@Data
public class AuthRspDto extends BaseDto {
    // TODO 补充额外小程序需要字段
    private UserType uType; // 用户类型，医生/体检人
    private int isValidDoctor; // 医生权限是否已生效
    private DepartmentPermission permission; // 科室权限类型，全科室/外科/口腔科...

    public AuthRspDto() {
    }

    public AuthRspDto(String openId, String userId) {
        this.setOpenId(openId);
        this.setUserId(userId);
    }
}
