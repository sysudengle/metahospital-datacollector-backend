package com.metahospital.datacollector.controller;

import com.metahospital.datacollector.common.RestResponse;
import com.metahospital.datacollector.controller.dto.AuthReqDto;
import com.metahospital.datacollector.controller.dto.AuthRspDto;
import com.metahospital.datacollector.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2021/12/12.
 */


@Controller
public class UserController {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private LoginService loginService;

    @PostMapping("wx/login")
    @ResponseBody
    public RestResponse<AuthRspDto> user_login(@RequestParam(value = "code") String jsCode, HttpServletRequest request) {
        AuthReqDto authReqDto = new AuthReqDto();
        authReqDto.setWechatJsCode(jsCode);
        String sessionId = request.getSession(true).getId();
        LOGGER.info("auth api sessionid: " + sessionId);
        return new RestResponse<>(loginService.UserMergeData(jsCode,sessionId));
    }
}



