package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.UserType;
import lombok.Data;

@Data
public class AuthReqDto {
    private String wechatJsCode;
    private String staffId;
    private UserType uType;
}
