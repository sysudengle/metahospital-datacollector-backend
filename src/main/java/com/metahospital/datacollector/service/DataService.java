/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.service;

import com.metahospital.datacollector.common.RestResponse;
import com.metahospital.datacollector.controller.dto.*;

import java.util.List;
import java.util.function.Supplier;

public interface DataService {
    AuthRspDto authWX(AuthReqDto authReqDto);
    RegisterWXDoctorRspDto registerDoctor(RegisterWXDoctorReqDto registerWXDoctorReqDto);
    GetWXDoctorRspDto getDoctor(GetWXDoctorReqDto getWXDoctorReqDto);
    List<HospitalDto> getHospitals();
    AddWXProfileRspDto upsertProfile(AddWXProfileReqDto addWXProfileReqDto);
    GetWXProfilesRspDto getProfiles(GetWXProfilesReqDto getWXProfilesReqDto);
    AddWXBookingRspDto upsertBooking(AddWXBookingReqDto addWXBookingReqDto);
    CompleteWXBookingRspDto completeBooking(CompleteWXBookingReqDto completeWXBookingReqDto);
    GetWXBookingsRspDto getBookings(GetWXBookingsReqDto getWXBookingsReqDto);
    GetWXBookingDetailRspDto getBookingDetail(GetWXBookingDetailReqDto getWXBookingDetailReqDto);
    List<DepartmentDto> getDepartments(GetWXDepartmentsReqDto getWXDepartmentsReqDto);
    List<ItemValueDto> getItems(GetWXItemsReqDto getWXItemsReqDto);
    UpsertWXItemRspDto upsertItems(UpsertWXItemReqDto upsertWXItemReqDto);
    GetHospitalConfigRspDto getHospitalConfig(GetHospitalConfigReqDto getBookingConfigReqDto);
    GetDepartmentConfigRspDto getDepartmentConfig(GetDepartmentConfigReqDto getDepartmentConfigReqDto);
    GetComboConfigRspDto getComboConfig(GetComboConfigReqDto getComboConfigReqDto);
    GetItemConfigRspDto getItemConfig(GetItemConfigReqDto getItemConfigReqDto);
    <RES, REQ extends BaseDto> RestResponse<RES> authentication(REQ reqDto, Supplier<RES> resDtoSupplier);
}
