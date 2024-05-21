package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiInvokeStatus {
    PENDING(1), // 待处理
    VALID(2),   // 有效期内
    EXPIRED(3), // 已过期
    SUSPENDED(4),   // 暂停, 可恢复
    CANCELED(5);  // 取消, 不可恢复

    @EnumValue
    private final int code;
}
