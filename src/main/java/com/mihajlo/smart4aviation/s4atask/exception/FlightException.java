package com.mihajlo.smart4aviation.s4atask.exception;

import com.mihajlo.smart4aviation.s4atask.exception.config.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FlightException extends BaseException {

    public FlightException(String message) {
        super(ErrorCode.FLIGHT_EXCEPTION, message);
    }
}