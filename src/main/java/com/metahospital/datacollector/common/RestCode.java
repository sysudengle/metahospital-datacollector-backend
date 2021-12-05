/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.common;

public class RestCode {
    public static final int UNKONW_ERROR = 101;

    public static final int SUCCESS = 200;

    // 400自增值为客户端错误
    public static final int BIZ_ARGUMENT_INVALID = 400;

    // 500自增值为后台错误
    public static final int DB_REQ_TIMEOUT_ERROR = 500;
}
