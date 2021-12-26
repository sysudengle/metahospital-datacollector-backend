package com.metahospital.datacollector.controller.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetWXProfilesRspDto {
    private List<ProfileInfoDto> profiles;
}
