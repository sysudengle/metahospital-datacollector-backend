package com.metahospital.datacollector.service;

import com.metahospital.datacollector.controller.dto.AuthRspDto;

@Deprecated
public interface LoginService {
    AuthRspDto UserMergeData(String wechatJsCode, String sessionId);

}
