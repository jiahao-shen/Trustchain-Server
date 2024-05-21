package com.hawkeye.model.enums;

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
    PATCH(5),
    HEAD(6),
    TRACE(7),
    CONNECT(8),
    OPTIONS(9);

    @EnumValue
    private final int code;
}
