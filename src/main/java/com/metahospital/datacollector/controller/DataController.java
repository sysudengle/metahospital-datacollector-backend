/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.controller;

import com.alibaba.fastjson.JSONObject;
import com.metahospital.datacollector.common.RestResponse;
import com.metahospital.datacollector.common.WechatUtil;
import com.metahospital.datacollector.controller.dto.AuthReqDto;
import com.metahospital.datacollector.controller.dto.AuthRspDto;
import com.metahospital.datacollector.dao.WechatAccountDao;
import com.metahospital.datacollector.dao.entity.User;
import com.metahospital.datacollector.dao.entity.WechatAccount;
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
import java.util.Random;

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
        AuthRspDto rspDto = new AuthRspDto("aaa","bbb",123456);
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


    // todo 判断前端再次post的userid和sessionid是否有效
    //前端需要判断自己有没有userid 和 sessionid
    //若没有，则走login端口
    //若有，则走check端口。如果存在，则可以继续操作；如果不存在，返回登录信息已过期请重新登录。
    public void accountCheckService(){}

}
