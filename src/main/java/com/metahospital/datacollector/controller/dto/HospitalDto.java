package com.metahospital.datacollector.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HospitalDto extends BaseDto{
    private int hospitalId = -1; // 医院id
    private String name; // 医院名称
}
