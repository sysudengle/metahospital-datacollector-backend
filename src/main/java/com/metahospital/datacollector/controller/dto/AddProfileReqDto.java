package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.ProfileInfo;
import lombok.Data;

@Data
public class AddProfileReqDto extends BaseDto {
    private ProfileInfo profileInfo;
}
