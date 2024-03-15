package com.trustchain.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpBodyRawType {
    TEXT(1),
    JSON(2),
    JAVASCRIPT(3),
    HTML(4),
    XML(5);

    @EnumValue
    private final int code;
}
