package com.metahospital.datacollector.controller.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthRspDto {
    @Setter
    @Getter
    private String openId;

    @Setter
    @Getter
    private long userId;

    @Setter
    @Getter
    private String sessionId;

    public AuthRspDto() {
    }

    public AuthRspDto(String openId, String sessionId, long userId) {
        this.openId = openId;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    // TODO 补充额外小程序需要字段
}
