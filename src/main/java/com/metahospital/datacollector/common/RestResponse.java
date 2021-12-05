/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.common;

import com.metahospital.datacollector.aop.handler.ClientException;
import com.metahospital.datacollector.aop.handler.ServerException;

// 接口回包信息
public class RestResponse<T> {

    public static final RestResponse SUCCESS = new RestResponse();

    public static final RestResponse ERROR_UNKNOWN = new RestResponse(RestCode.UNKONW_ERROR, "系统异常！");

    private static final String EMPTY_STRING = "";

    private int retcode = RestCode.SUCCESS;
    private String message = EMPTY_STRING;
    private T data;

    public RestResponse() {}

    public RestResponse(int retcode, String message) {
        this.retcode = retcode;
        this.message = message;
    }

    public RestResponse(T data) {
        this.data = data;
    }

    public static RestResponse processErrorResponse(Exception ex) {
        RestResponse res = new RestResponse();

        if (ex instanceof ClientException) {
            res.setRetcode(RestCode.BIZ_ARGUMENT_INVALID);
        } else if (ex instanceof ServerException) {
            res.setRetcode(RestCode.DB_REQ_TIMEOUT_ERROR);
        } else if (ex instanceof IllegalArgumentException) {
            res.setRetcode(RestCode.BIZ_ARGUMENT_INVALID);
        } else {
            res.setRetcode(RestCode.UNKONW_ERROR);
        }

        res.setMessage(ex.getMessage());

        return res;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
