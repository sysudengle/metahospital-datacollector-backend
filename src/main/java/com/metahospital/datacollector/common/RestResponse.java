/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.common;

import com.metahospital.datacollector.aop.handler.CollectorException;

// 接口回包信息
public class RestResponse<T> {
    private static final String EMPTY_STRING = "";

    private int retcode = RestCode.SUCCESS.getValue();
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

        if (ex instanceof CollectorException) {
            CollectorException cEx = (CollectorException) ex;
            res.setRetcode(cEx.getRestCode());
        } else {
            res.setRetcode(RestCode.UNKONW_ERR.getValue());
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
