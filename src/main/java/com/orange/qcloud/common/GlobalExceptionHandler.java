package com.orange.qcloud.common;

import com.orange.qcloud.common.ApiException;
import com.orange.qcloud.common.ApiResponse;
import com.orange.qcloud.common.EnumError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse<Object> doError(HttpServletRequest servletRequest,
                               HttpServletResponse httpServletResponse,
                               Exception ex) {
        if (ex instanceof ApiException e) {
            return ApiResponse.error(e.getErrCode(), e.getErrMsg());
        } else if (ex instanceof NoHandlerFoundException) {
            return ApiResponse.error(EnumError.NO_HANDLER_FOUND);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return ApiResponse.error(EnumError.BIND_EXCEPTION_ERROR);
        } else if (ex instanceof BadCredentialsException) {
            return ApiResponse.error(EnumError.ERROR_EMAIL_PASSWORD);
        } else {
            log.error(ex.getClass() + ": " + ex.getMessage());
            return ApiResponse.error(EnumError.UNKNOWN_ERROR);
        }
    }
}
