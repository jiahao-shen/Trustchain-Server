package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.EnumValue;


@Getter
@AllArgsConstructor
public enum OrganizationType {
    PUBLIC(1),
    MEDICAL(2),
    EDUCATION(3),
    FINANCIAL(4),
    GOVERNMENT(5),
    TECHNOLOGY(6);

    @EnumValue
    private final int code;
}
