package com.orange.qcloud.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private Integer errCode;
    private String errMsg;

    public ApiException(EnumError enumError) {
        super();
        this.errCode = enumError.getErrCode();
        this.errMsg = enumError.getErrMsg();
    }

    public ApiException(Integer errCode, String errMsg) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
