package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum RegisterStatus {
    PENDING(1),
    ALLOW(2),
    REJECT(3);

    @EnumValue
    private final int code;
}
