package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiVisible {
    PRIVATE(1), // 机构内部可见
    PUBLIC(2);  // 所有人可见

    @EnumValue
    private final int code;
}
