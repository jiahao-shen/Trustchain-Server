package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletState {
    DISABLE(0),     // 禁用
    ENABLE(1);      // 启用

    @EnumValue
    private final int code;
}
