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
import com.metahospital.datacollector.service.DataService;
import com.metahospital.datacollector.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/test")
@RestController
@PropertySource("classpath:application.properties")
public class TestController {
    public static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;
    @Autowired
    private DataService dataService;

    @GetMapping(value = "/holly/test", produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<String> hollytest(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name) {
        return new RestResponse<>(testService.testMergeData(id, name));
    }

    // 微信登陆鉴权接口, 废弃
    @Deprecated
    @PostMapping(value = "/auth_wx", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AuthRspDto> authWXOld(@RequestBody AuthReqDto authReqDto) {
        AuthRspDto rspDto = dataService.authWX(authReqDto);
        return new RestResponse<>(rspDto);
    }
}
