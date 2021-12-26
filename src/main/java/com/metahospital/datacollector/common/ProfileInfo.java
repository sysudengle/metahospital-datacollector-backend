package com.metahospital.datacollector.common;

import lombok.Data;

@Data
public class ProfileInfo {
    private int profileId = -1; // 写操作自行生成
    private int hospitalId; // 医院ID
    private int personalID; // 身份证号码
    private Gender gender; // 性别
    private String address; // 地址
}
