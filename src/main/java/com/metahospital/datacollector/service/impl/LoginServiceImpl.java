package com.metahospital.datacollector.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.metahospital.datacollector.common.enums.UserType;
import com.metahospital.datacollector.common.util.WechatUtil;
import com.metahospital.datacollector.controller.dto.AuthRspDto;
import com.metahospital.datacollector.dao.UserDao;
import com.metahospital.datacollector.dao.WechatAccountDao;
import com.metahospital.datacollector.dao.data.User;
import com.metahospital.datacollector.dao.data.WechatAccount;
import com.metahospital.datacollector.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@Deprecated
public class LoginServiceImpl implements LoginService {

    @Autowired
    private WechatAccountDao wechatAccountDao;
    @Autowired
    private UserDao userDao;

    public static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);


    @Override
    public AuthRspDto UserMergeData(String wechatJsCode, String sessionId) {

        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(wechatJsCode);
        String openId = SessionKeyOpenId.getString("openid");
        //String unionid = SessionKeyOpenId.getString("unionid");//上了平台才会有值，私人开发返回null
        String unionId = "aaa";
        String sessionKey = SessionKeyOpenId.getString("session_key");
        long userId = new Random().nextLong();                   //暂时先用随机长型代替
        String name = "lee";                                     //暂时先用字符表示，是否从前端微信用户名入手？？
        userDao.replace(new User(userId, name + userId, UserType.Patient.getType()));
        User user = userDao.get(userId);
        wechatAccountDao.replace(new WechatAccount(openId, unionId, sessionKey, user.getUserId()));

        return new AuthRspDto(openId,String.valueOf(userId));
    }
}
