package com.trustchain.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;


@Getter
@AllArgsConstructor
public enum OrganizationType {
    PUBLIC(1),      // 公共
    MEDICAL(2),     // 医疗
    EDUCATION(3),   // 教育
    FINANCIAL(4),   // 金融
    GOVERNMENT(5),  // 政府
    TECHNOLOGY(6);  // 科技

    @EnumValue
    private final int code;
}
