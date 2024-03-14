package com.trustchain.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum HttpMethod {
    GET(1),
    POST(2),
    PUT(3),
    DELETE(4),
    TRACE(5),
    CONNECT(6),
    HEAD(7),
    OPTIONS(8);

    @EnumValue
    private final int code;
}
