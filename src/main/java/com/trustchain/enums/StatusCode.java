package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS(1000),
    LOGIN_FAILED(1001),
    RESET_PASSWORD_FAILED(1002),
    UPLOAD_FILE_FAILED(1003),
    REGISTER_FAILED(1004);

    @EnumValue
    private final int code;
}
