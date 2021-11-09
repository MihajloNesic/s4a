package com.mihajlo.smart4aviation.s4atask.exception.config;

public enum ErrorCode {
    BASE_EXCEPTION(1000),
    FLIGHT_EXCEPTION(2000);

    private final Integer code;

    ErrorCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}