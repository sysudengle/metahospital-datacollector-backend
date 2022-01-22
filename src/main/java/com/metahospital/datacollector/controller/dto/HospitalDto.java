package com.metahospital.datacollector.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDto {
    /** 医院id */
    private int hospitalId;
    /** 医院名称 */
    private String name;
}
