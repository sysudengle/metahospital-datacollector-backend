/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.common;

import com.metahospital.datacollector.aop.handler.ClientException;
import com.metahospital.datacollector.aop.handler.ServerException;

// 接口回包信息
public class RestResponse<T> {

    public static final RestResponse SUCCESS = new RestResponse();

    public static final RestResponse ERROR_UNKNOWN = new RestResponse(RestErrorCode.UNKONW_ERROR, "系统异常！");

    private static final String EMPTY_STRING = "";

    private int errorCode = RestErrorCode.SUCCESS;
    private String errorMessage = EMPTY_STRING;
    private T data;

    public RestResponse() {}

    public RestResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public RestResponse(T data) {
        this.data = data;
    }

    public static RestResponse processErrorResponse(Exception ex) {
        RestResponse res = new RestResponse();

        if (ex instanceof ClientException) {
            res.setErrorCode(RestErrorCode.BIZ_ARGUMENT_INVALID);
        } else if (ex instanceof ServerException) {
            res.setErrorCode(RestErrorCode.SERVER_SIDE_ERROR);
        } else if (ex instanceof IllegalArgumentException) {
            res.setErrorCode(RestErrorCode.BIZ_ARGUMENT_INVALID);
        } else {
            res.setErrorCode(RestErrorCode.UNKONW_ERROR);
        }

        res.setErrorMessage(ex.getMessage());

        return res;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
