/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Last modified: 2021-12-18 22:28
 * Author: allendeng
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.controller;

import com.metahospital.datacollector.common.RestResponse;
import com.metahospital.datacollector.controller.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.metahospital.datacollector.service.DataService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@PropertySource("classpath:application.properties")
public class DataController {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private DataService dataService;

    /**
     * 微信登陆鉴权接口
     */
    @PostMapping(value = "wx/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AuthRspDto> authWX(@RequestBody AuthReqDto authReqDto) {
        return new RestResponse<>(dataService.authWX(authReqDto));
    }

    /**
     * 微信医生注册接口
     */
    @PostMapping(value = "wx/doctor/register", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<RegisterWXDoctorRspDto>  registerDoctorWX(@RequestBody RegisterWXDoctorReqDto registerReqDto) {
        return dataService.authentication(registerReqDto, () -> dataService.registerDoctor(registerReqDto));
    }

    /**
     * 微信医生获取接口
     */
    @PostMapping(value = "wx/doctor", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<GetWXDoctorRspDto> getDoctorWX(@RequestBody GetWXDoctorReqDto getDoctorReqDto) {
        return dataService.authentication(getDoctorReqDto, () -> dataService.getDoctor(getDoctorReqDto));
    }

    /**
     * 新建或修改档案接口
     */
    @PostMapping(value = "wx/profile/upsert", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AddWXProfileRspDto> upsertWXProfile(@RequestBody AddWXProfileReqDto addWXProfileReqDto) {
        return dataService.authentication(addWXProfileReqDto, () -> dataService.upsertProfile(addWXProfileReqDto));
    }

    /**
     * 获取所有档案接口
     */
    @PostMapping(value = "wx/profiles", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<GetWXProfilesRspDto> getWXProfiles(@RequestBody GetWXProfilesReqDto getWXProfilesReqDto) {
        return dataService.authentication(getWXProfilesReqDto, () -> dataService.getProfiles(getWXProfilesReqDto));
    }

    /**
     * 新建或修改预约接口
     */
    @PostMapping(value = "wx/booking/upsert", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AddWXBookingRspDto> upsertBook(@RequestBody AddWXBookingReqDto addWXBookingReqDto) {
        return dataService.authentication(addWXBookingReqDto, () -> dataService.upsertBooking(addWXBookingReqDto));
    }

    /**
     * 完成预约接口
     */
    @PostMapping(value = "wx/booking/complete", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<CompleteWXBookingRspDto> completeBooking(@RequestBody CompleteWXBookingReqDto completeWXBookingReqDto) {
        return dataService.authentication(completeWXBookingReqDto, () -> dataService.completeBooking(completeWXBookingReqDto));
    }

    /**
     * 根据档案获取所有预约接口
     */
    @PostMapping(value = "/wx/bookings", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<GetWXBookingsRspDto> getBookings(@RequestBody GetWXBookingsReqDto getWXBookingsReqDto) {
        return dataService.authentication(getWXBookingsReqDto, () -> dataService.getBookings(getWXBookingsReqDto));
    }

    /**
     * 根据预约id获取详细的预约信息
     */
    @PostMapping(value = "/wx/booking/detail", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<GetWXBookingDetailRspDto> getBooking(@RequestBody GetWXBookingDetailReqDto getWXBookingDetailReqDto) {
        return dataService.authentication(getWXBookingDetailReqDto, () -> dataService.getBookingDetail(getWXBookingDetailReqDto));
    }

    /**
     * todo why == 未实现接口，暂时保留，希望让前端改从配置数据接口拉取数据，需要和前端协商
     * 根据预约id获取所有科室信息接口
     */
    @PostMapping(value = "/wx/booking/departments", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<List<DepartmentDto>> getDepartments(@RequestBody GetWXDepartmentsReqDto getWXDepartmentsReqDto) {
        return dataService.authentication(getWXDepartmentsReqDto, () -> dataService.getDepartments(getWXDepartmentsReqDto));
    }

    /**
     * 根据预约id和科室id，获取对应体检项详情
     */
    @PostMapping(value = "/wx/booking/department/items", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<List<ItemValueDto>> getItems(@RequestBody GetWXItemsReqDto getWXItemsReqDto) {
        return dataService.authentication(getWXItemsReqDto, () -> dataService.getItems(getWXItemsReqDto));
    }

    /**
     * 提交体检项目值
     */
    @PostMapping(value = "/wx/booking/department/items/upsert", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<UpsertWXItemRspDto> upsertItems(@RequestBody UpsertWXItemReqDto upsertWXItemReqDto) {
        return dataService.authentication(upsertWXItemReqDto, () -> dataService.upsertItems(upsertWXItemReqDto));
    }

    /**
     * 获取医院配置数据接口
     */
    @PostMapping(value = "/config/hospital", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public RestResponse<GetHospitalConfigRspDto> getHospitalConfig(@RequestBody GetHospitalConfigReqDto getHospitalConfigReqDto) {
        return dataService.authentication(getHospitalConfigReqDto, () -> dataService.getHospitalConfig(getHospitalConfigReqDto));
    }

    /**
     * 获取科室配置数据接口
     */
    @PostMapping(value = "/config/department", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public RestResponse<GetDepartmentConfigRspDto> getDepartmentConfig(@RequestBody GetDepartmentConfigReqDto getDepartmentConfigReqDto) {
        return dataService.authentication(getDepartmentConfigReqDto, () -> dataService.getDepartmentConfig(getDepartmentConfigReqDto));
    }

    /**
     * 获取套餐配置数据接口
     */
    @PostMapping(value = "/config/combo", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public RestResponse<GetComboConfigRspDto> getComboConfig(@RequestBody GetComboConfigReqDto getComboConfigReqDto) {
        return dataService.authentication(getComboConfigReqDto, () -> dataService.getComboConfig(getComboConfigReqDto));
    }

    /**
     * 获取指标项配置数据接口
     */
    @PostMapping(value = "/config/item", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public RestResponse<GetItemConfigRspDto> getItemConfig(@RequestBody GetItemConfigReqDto getItemConfigReqDto) {
        return dataService.authentication(getItemConfigReqDto, () -> dataService.getItemConfig(getItemConfigReqDto));
    }


    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}
