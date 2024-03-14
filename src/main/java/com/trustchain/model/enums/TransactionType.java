package com.trustchain.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
    TOPUP(1),       // 充值
    WITHDRAW(2),    // 提现
    INCOME(3),      // 收入
    EXPENSE(4);     // 支出

    @EnumValue
    private final int code;
}
