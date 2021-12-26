package com.metahospital.datacollector.common;

import lombok.Data;

@Data
public class DossierInfo {
    private int hospitalId; // 医院ID
    private int IDNumber; // 身份证号码
    private Gender gender; // 性别
    private String address; // 地址
}
