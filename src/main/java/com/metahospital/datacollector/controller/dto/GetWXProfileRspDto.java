package com.metahospital.datacollector.controller.dto;

import java.util.Map;

import com.metahospital.datacollector.common.ProfileInfo;
import lombok.Data;

@Data
public class GetWXProfileRspDto {
    private Map<Integer, ProfileInfo> dossierInfoMap;
}
