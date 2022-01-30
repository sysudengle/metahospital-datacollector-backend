package com.metahospital.datacollector.controller.dto;

import lombok.Data;

/**
 * 保留用于后续添加字段
 */
@Data
public class AddWXProfileRspDto extends BaseRspDto {
    private ProfileInfoDto profile;

    public AddWXProfileRspDto(ProfileInfoDto profile) {
        this.profile = profile;
    }
}
