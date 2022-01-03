package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileInfoDto {
    private long profileId = -1; // 写操作自行生成
    private int hospitalId; // 医院ID
    private String personalID; // 身份证号码
    private Gender gender; // 性别
    private String pidAddress; // 身份证地址
    private String homeAddress; // 家庭住址
}
