package com.hawkeye.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CertificateStatus {
    IS_DELIVERED(1),
    IS_RETURNED(2);

    @EnumValue
    private final int code;
}
