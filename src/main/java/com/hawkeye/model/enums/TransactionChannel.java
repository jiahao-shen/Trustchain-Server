package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionChannel {
    ALIPAY(1),  // 支付宝
    WECHAT(2),  // 微信
    API_INVOKE(3); // API调用

    @EnumValue
    private final int code;
}
