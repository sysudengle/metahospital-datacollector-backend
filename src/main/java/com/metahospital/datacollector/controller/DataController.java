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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@PropertySource("classpath:application.properties")
public class DataController {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private DataService dataService;


    @GetMapping(value = "/holly/test", produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<String> hollytest(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name) {
        return new RestResponse<>(dataService.testMergeData(id, name));
    }

    // 微信登陆鉴权接口, 废弃
    @Deprecated
    @PostMapping(value = "/auth_wx", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AuthRspDto> authWXOld(@RequestBody AuthReqDto authReqDto) {
        AuthRspDto rspDto = dataService.authWX(authReqDto);
        return new RestResponse<>(rspDto);
    }

    // 微信登陆鉴权接口
    @PostMapping(value = "wx/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AuthRspDto> authWX(@RequestBody AuthReqDto authReqDto) {
        AuthRspDto rspDto = dataService.authWX(authReqDto);
        return new RestResponse<>(rspDto);
    }

    // 微信医生注册接口
    @PostMapping(value = "wx/doctor/register", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<RegisterWXDoctorRspDto>  registerDoctorWX(@RequestBody RegisterWXDoctorReqDto registerReqDto) {
        RegisterWXDoctorRspDto rspDto = dataService.registerDoctor(registerReqDto);
        return new RestResponse<>(rspDto);
    }

    // 微信医生获取接口
    @PostMapping(value = "wx/doctor", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<GetWXDoctorRspDto>  getDoctorWX(@RequestBody GetWXDoctorReqDto getDoctorReqDto) {
        GetWXDoctorRspDto rspDto = dataService.getDoctor(getDoctorReqDto);
        return new RestResponse<>(rspDto);
    }

    // 微信获取医院接口
    @GetMapping(value = "wx/hospitals", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public RestResponse<List<HospitalDto>> getWXHospitals() {
        List<HospitalDto> hospitalDtos = dataService.getHospitals();
        return new RestResponse<>(hospitalDtos);
    }

    // 新建档案接口
    @PostMapping(value = "wx/profile/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AddWXProfileRspDto> addWXProfile(@RequestBody AddWXProfileReqDto addWXProfileReqDto) {
        AddWXProfileRspDto rspDto = dataService.addProfile(addWXProfileReqDto);
        return new RestResponse<>(rspDto);
    }

    // 根据微信账号获取所有档案接口
    @PostMapping(value = "wx/profiles", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<GetWXProfilesRspDto> getWXProfiles(@RequestBody GetWXProfilesReqDto getWXProfilesReqDto) {
        GetWXProfilesRspDto rspDto = dataService.getProfiles(getWXProfilesReqDto);
        return new RestResponse<>(rspDto);
    }

    // 新建预约接口
    @PostMapping(value = "wx/booking/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AddWXBookingRspDto> addBook(@RequestBody AddWXBookingReqDto appointmentReqDto) {
        AddWXBookingRspDto rspDto = dataService.addBooking(appointmentReqDto);
        return new RestResponse<>(rspDto);
    }

    // 根据档案获取所有预约接口
    @PostMapping(value = "/wx/bookings", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<GetWXBookingsRspDto> getBooks(@RequestBody GetWXBookingsReqDto getWXBookingsReqDto) {
        GetWXBookingsRspDto rspDto = dataService.getBookings(getWXBookingsReqDto);
        return new RestResponse<>(rspDto);
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    // todo 判断前端再次post的userid和sessionid是否有效
    //前端需要判断自己有没有userid 和 sessionid
    //若没有，则走login端口
    //若有，则走check端口。如果存在，则可以继续操作；如果不存在，返回登录信息已过期请重新登录。
    public void accountCheckService(){}

}
