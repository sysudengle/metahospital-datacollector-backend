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
import com.metahospital.datacollector.controller.dto.AuthReqDto;
import com.metahospital.datacollector.controller.dto.AuthRspDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.metahospital.datacollector.service.DataService;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

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

    // 微信登陆鉴权接口
    @PostMapping(value = "/auth_wx", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AuthRspDto> authWX(@RequestBody AuthReqDto authReqDto, HttpServletRequest request) {
        AuthRspDto rspDto = dataService.authWX(authReqDto);
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
