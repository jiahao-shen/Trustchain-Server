package com.trustchain.model.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateRange {
    LAST_WEEK(1), // 过去一周
    LAST_MONTH(2),  // 过去一月
    LAST_YEAR(3);   // 过去一年

    @EnumValue
    private final int code;
}
