package com.hawkeye.model.vo;

import com.hawkeye.model.enums.HttpBodyRawType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRawBodyVO {
    private HttpBodyRawType type;

    private String body;
}
