package com.trustchain.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum ApiInvokeMethod {
    WEB(1),
    SDK(2);
//    JAVA_SDK(2),
//    PYTHON_SDK(3),
//    GO_SDK(4);

    @EnumValue
    private final int code;
}
