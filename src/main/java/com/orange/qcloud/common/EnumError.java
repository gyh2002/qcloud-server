package com.orange.qcloud.common;

import lombok.Getter;

@Getter
public enum EnumError {
    UNKNOWN_ERROR(10001, "Unknown error"),
    NO_HANDLER_FOUND(10002, "No handler found"),
    BIND_EXCEPTION_ERROR(10003, "Request parameter error"),
    UNAUTHORIZED_ERROR(10004, "Unauthorized"),
    INVALID_JWT_TOKEN(10005, "Invalid token");

    private final Integer errCode;
    private final String errMsg;

    EnumError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
