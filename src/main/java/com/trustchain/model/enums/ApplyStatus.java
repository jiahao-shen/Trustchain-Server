package com.trustchain.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum ApplyStatus {
    PENDING(1),     // 待处理
    ALLOW(2),       // 允许
    REJECT(3);      // 驳回

    @EnumValue
    private final int code;
}
