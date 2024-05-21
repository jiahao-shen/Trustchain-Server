package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpResponseBodyType {
    NONE(1),
//    FORM_DATA(2),
//    X_WWW_FORM_URLENCODED(3),
    RAW(4),
    BINARY(5),
    GRAPHQL(6);

    @EnumValue
    private final int code;
}
