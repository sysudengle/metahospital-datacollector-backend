package com.metahospital.datacollector.controller.dto;

import java.util.Map;

import com.metahospital.datacollector.common.DossierInfo;
import lombok.Data;

@Data
public class GetDossierWithWXRspDto {
    private Map<Integer, DossierInfo> dossierInfoMap;
}
