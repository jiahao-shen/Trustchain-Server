package com.hawkeye.model.vo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiHeaderItemVO {
    private String key;
    private String type;
    private String value;
    private Boolean required;
    private String description;
}
