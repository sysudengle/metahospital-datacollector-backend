package com.metahospital.datacollector.controller.dto;

import lombok.Data;

@Data
public class HospitalDto {
    private long hospitalId = -1; // 医院id
    private String name; // 医院名称
}
