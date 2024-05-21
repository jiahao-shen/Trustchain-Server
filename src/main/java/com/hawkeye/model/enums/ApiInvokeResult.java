package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiInvokeResult {
    SUCCESS(1),
    FAILED(2);

    @EnumValue
    private final int code;
}
