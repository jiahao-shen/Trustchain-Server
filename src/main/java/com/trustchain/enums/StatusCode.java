package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS(1000),
    LOGIN_FAILED(1001),
    RESET_PASSWORD_FAILED(1002);


    @EnumValue
    private final int code;
}
