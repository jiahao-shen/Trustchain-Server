package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiInvokeRange {
    SELF(1),
    INTERNAL(2);

    @EnumValue
    private final int code;
}
