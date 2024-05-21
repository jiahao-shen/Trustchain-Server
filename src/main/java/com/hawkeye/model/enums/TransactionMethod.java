package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionMethod {
    TOPUP(1),   // 充值

    WITHDRAW(2),    // 提现

    TRANSFER(3),    // 转账

    REFUND(4),      // 退款

    PURCHASE(5),    // 购买

    SALE(6);    // 销售

    @EnumValue
    private final int code;
}
