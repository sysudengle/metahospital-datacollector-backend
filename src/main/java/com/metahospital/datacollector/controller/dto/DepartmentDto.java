package com.metahospital.datacollector.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepartmentDto {
    private int departmentId;
    private String departmentName;
}
