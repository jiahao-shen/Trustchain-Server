package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceType {
    INCOME(1),      // 收入
    EXPENSE(2);     // 支出

    @EnumValue
    private final int code;
}
