/*
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 */
package com.metahospital.datacollector.aop.handler;

public class ClientException extends RuntimeException {
    public static final String INVALID_PARAM = "Invalid param";

    public ClientException(String errorMsg) {
        super(errorMsg);
    }
}
