package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoDto {
    /** 档案Id，写操作不需要填写，后端自行生成 */
    private long profileId = -1;
    /** 医院Id */
    private int hospitalId;
    /** 身份证号码 */
    private String personalID;
    /** 性别 */
    private Gender gender;
    /** 身份证地址 */
    private String pidAddress;
    /** 家庭住址 */
    private String homeAddress;
}
