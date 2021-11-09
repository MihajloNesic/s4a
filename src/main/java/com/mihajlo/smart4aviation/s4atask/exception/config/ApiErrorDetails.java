package com.mihajlo.smart4aviation.s4atask.exception.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorDetails {
    private Integer status;
    private String message;
    private String messageDetails;
    private String path;
    private String exceptionClassName;
}
