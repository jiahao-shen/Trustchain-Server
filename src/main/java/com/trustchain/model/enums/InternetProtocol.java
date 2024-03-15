package com.trustchain.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternetProtocol {
    HTTP(1),
    HTTPS(2);

    @EnumValue
    private final int code;
}
