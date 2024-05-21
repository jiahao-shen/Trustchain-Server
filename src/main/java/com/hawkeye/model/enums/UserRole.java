package com.hawkeye.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN(1),       // 管理员用户
    COMMON(2);      // 普通用户

    @EnumValue
    private final int code;
}
