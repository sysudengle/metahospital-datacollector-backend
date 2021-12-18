/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 * -----
 * Last Modified: 2021-12-10 15:24:47
 * Modified By: laynexie
 * -----
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 * -----
 */
package com.metahospital.datacollector.common;

public enum RestCode {
    UNKONW_ERR(-1),
    SUCCESS(200),
    // 400~499 返回码区间表示客户端错误
    PARAM_INVALID_ERR(400),

    // 500～600 返回码区间表示服务端错误
    DB_REQ_TIMEOUT_ERR(500),
    REDIS_REQ_ERR(501),
    LOGIC_PROCESS_ERR(502),
    WECHAT_OPENID_INVALID_ERR(503);

    private final int value;
    RestCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
