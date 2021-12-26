/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.service;

import com.metahospital.datacollector.controller.dto.*;

public interface DataService {
    String testMergeData(String id, String name);

    AuthRspDto authWX(AuthReqDto authReqDto);

    AddDossierRspDto addDossier(AddDossierReqDto addDossierReqDto);
    GetDossierWithWXRspDto getDossierWithWX(GetDossierWithWXReqDto getDossierWithWXReqDto);

    AddAppointmentRspDto addAppointment(AddAppointmentReqDto addAppointmentReqDto);
    GetAppointmentRspDto getAppointment(GetAppointmentReqDto getAppointmentRspDto);
}
