package com.metahospital.datacollector.controller.dto;

import java.util.List;
import java.util.Map;

import com.metahospital.datacollector.common.ProfileInfo;
import lombok.Data;

@Data
public class GetWXProfilesRspDto {
    private List<ProfileInfo> profiles;
}
