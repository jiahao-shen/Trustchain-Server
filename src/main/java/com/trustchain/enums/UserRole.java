package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN(1),
    NORMAL(2);

    @EnumValue
    private final int code;
}
