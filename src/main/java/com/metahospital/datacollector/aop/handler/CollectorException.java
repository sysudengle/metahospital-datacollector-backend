/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.aop.handler;

import com.metahospital.datacollector.common.RestCode;

// ATTENTION! 统一使用这个作为异常抛出类!
public class CollectorException extends RuntimeException {
    private RestCode restCode;

    public CollectorException(RestCode restCode) {
        super("");
        this.restCode = restCode;
    }

    public CollectorException(RestCode restCode, String errorMsg) {
        super(errorMsg);
        this.restCode = restCode;
    }

    public int getRestCode() {
        return restCode.getValue();
    }
}
