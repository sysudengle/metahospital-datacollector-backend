package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public abstract class BaseDto {
    private String openId;
    private long userId;
}
