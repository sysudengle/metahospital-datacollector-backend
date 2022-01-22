package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class AuthReqDto {
    /** 微信登录时使用的code */
    private String wechatJsCode;
}
