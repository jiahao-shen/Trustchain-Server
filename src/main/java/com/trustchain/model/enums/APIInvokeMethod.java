package com.trustchain.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum APIInvokeMethod {
    WEB(1, "网页"),
    SDK(2, "SDK");

    @EnumValue
    private final int code;
    private final String descp;
}
