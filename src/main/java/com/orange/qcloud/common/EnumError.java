package com.orange.qcloud.common;

import lombok.Getter;

@Getter
public enum EnumError {
    UNKNOWN_ERROR(10001, "Unknown error"),
    NO_HANDLER_FOUND(10002, "No handler found"),
    BIND_EXCEPTION_ERROR(10003, "Request parameter error"),
    UNAUTHORIZED_ERROR(10004, "Unauthorized"),
    INVALID_JWT_TOKEN(10005, "Invalid token"),
    ERROR_EMAIL_PASSWORD(10006, "Email and password do not match"),
    VERIFICATION_CODE_DO_NOT_MATCH(10007, "Verification code do not match"),
    FILE_NOT_EXIST(20001, "File not exist"),
    FILE_ALREADY_EXIST(20002, "File already exist"),
    FOLDER_NOT_EXIST(20003, "Folder not exist"),
    FOLDER_ALREADY_EXIST(20004, "Folder already exist"),
    CREATE_FILE_FAILED(20005, "Create file failed"),
    CREATE_FOLDER_FAILED(20006, "Create folder failed"),
    DELETE_FILE_FAILED(20007, "Delete file failed"),
    DELETE_FOLDER_FAILED(20008, "Delete folder failed"),
    UPDATE_FILE_FAILED(20009, "Update file failed"),
    RENAME_FILE_FAILED(20010, "Rename file failed"),
    SAVE_IMAGE_FAILED(20011, "Save image failed"),
    GET_IMAGE_FAILED(20012, "Get image failed");

    private final Integer errCode;
    private final String errMsg;

    EnumError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
