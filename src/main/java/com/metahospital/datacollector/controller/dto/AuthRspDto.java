package com.metahospital.datacollector.controller.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthRspDto {
    @Setter
    @Getter
    private String openId;

    @Setter
    @Getter
    private String userId;

    @Setter
    @Getter
    private String sessionId;

    // TODO 补充额外小程序需要字段
}
