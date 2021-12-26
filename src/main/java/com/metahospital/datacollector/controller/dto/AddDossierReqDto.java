package com.metahospital.datacollector.controller.dto;

import com.metahospital.datacollector.common.DossierInfo;
import lombok.Data;

@Data
public class AddDossierReqDto extends BaseDto {
    private DossierInfo dossierInfo;
}
