package com.metahospital.datacollector.controller;

import com.alibaba.fastjson.JSONObject;
import com.metahospital.datacollector.common.WechatUtil;
import com.metahospital.datacollector.dao.WechatAccountDao;
import com.metahospital.datacollector.dao.entity.WechatAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;
/**
 * Created on 2021/12/12.
 */


@Controller
public class UserController {

    @Autowired
    private WechatAccountDao wechatAccountDao;
    @PostMapping("wx/login")
    @ResponseBody
    public void user_login(@RequestParam(value = "code", required = false) String code) {
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        String openid = SessionKeyOpenId.getString("openid");
        //String unionid = SessionKeyOpenId.getString("unionid");//上了平台才会有值，私人开发返回null
        String unionid = "aaa";
        String sessionKey = SessionKeyOpenId.getString("session_key");
        long userid = new Random().nextLong();
        WechatAccount wechatAccount = new WechatAccount(openid,unionid,sessionKey,userid);
        wechatAccountDao.replace(wechatAccount);

    }
}



