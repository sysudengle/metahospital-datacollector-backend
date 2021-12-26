package com.metahospital.datacollector.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2021/12/12.
 */

//前端调用需要在这里修改 appid和secret
public class WechatUtil {
    public static JSONObject getSessionKeyOrOpenId(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> requestUrlParam = new HashMap<>();
        //小程序appId//怕有危险的话，也可用@Value，这边是直接填写了
        requestUrlParam.put("appid", "wx0c29b965eafb3618");
        //小程序secret
        requestUrlParam.put("secret", "7ca79cd4d587924ff9ff2eecd0df285f");
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        return JSON.parseObject(HttpClientUtil.doPost(requestUrl, requestUrlParam));//提取键值可获得相关信息
    }

}

