/*
 * Created Date: 2021-12-10 14:28:46
 * Author: allendeng
 *
 * Copyright (C) 2021 MetaHospital, Inc. All Rights Reserved.
 *
 */
package com.metahospital.datacollector.common;

import org.springframework.util.ObjectUtils;

public class RestError {

    private final int errorCode;
    private final String errorMessage;

    public RestError(int code, String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof RestError) {
            RestError re = (RestError) o;
            return getErrorCode() == re.getErrorCode()
                    && ObjectUtils.nullSafeEquals(getErrorMessage(), re.getErrorMessage());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(new Object[] { getErrorCode(), getErrorMessage() });
    }

    public static class Builder {

        private int errorCode;
        private String errorMessage;

        public Builder() {
        }

        public Builder setErrorCode(int code) {
            this.errorCode = code;
            return this;
        }

        public Builder setErrorMessage(String message) {
            this.errorMessage = message;
            return this;
        }

        public RestError build() {
            return new RestError(this.errorCode, this.errorMessage);
        }
    }
}
