/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.aop.handler;

public class ServerException extends RuntimeException {
    public static final String REQUEST_ERROR = "Request error";

    public ServerException(String errorMsg) {
        super(errorMsg);
    }
}
