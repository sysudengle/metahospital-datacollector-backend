/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.service;

import com.metahospital.datacollector.controller.dto.*;

import java.util.List;

public interface DataService {
    String testMergeData(String id, String name);

    AuthRspDto authWX(AuthReqDto authReqDto);

    RegisterWXDoctorRspDto registerDoctor(RegisterWXDoctorReqDto registerWXDoctorReqDto);
    GetWXDoctorRspDto getDoctor(GetWXDoctorReqDto getWXDoctorReqDto);

    List<HospitalDto> getHospitals();

    AddWXProfileRspDto upsertProfile(AddWXProfileReqDto addWXProfileReqDto);
    GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto);

    AddWXBookingRspDto upsertBooking(AddWXBookingReqDto addWXBookingReqDto);
    GetWXBookingsRspDto getBookings(GetWXBookingsReqDto getAppointmentRspDto);
    List<DepartmentDto> getDepartments(GetWXDepartmentsReqDto getWXDepartmentsReqDto);
}
