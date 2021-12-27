package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.Gender;
import lombok.Data;

@Data
public class ProfileInfoDto {
    private int profileId = -1; // 写操作自行生成
    private int hospitalId; // 医院ID
    private int personalID; // 身份证号码
    private Gender gender; // 性别
    private String pidAddress; // 身份证地址
    private String homeAddress; // 家庭住址
}
