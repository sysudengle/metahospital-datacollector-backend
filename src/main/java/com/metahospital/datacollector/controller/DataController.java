/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
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
import javax.servlet.http.HttpSession;

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
        return new RestResponse<>(dataService.TestMergeData(id, name));
    }

    @PostMapping(value = "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResponse<AuthRspDto> auth(@RequestBody AuthReqDto authReqDto, HttpServletRequest request) {
        AuthRspDto rspDto = new AuthRspDto();

        // sessionId生成
        String sessionId = request.getSession(true).getId();
        LOGGER.info("auth api sessionid: " + sessionId);
        LOGGER.info("auth api received: " + authReqDto.getWechatJsCode());
        rspDto.setOpenId(authReqDto.getWechatJsCode() + "|processed");

        return new RestResponse<>(rspDto);
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

}
