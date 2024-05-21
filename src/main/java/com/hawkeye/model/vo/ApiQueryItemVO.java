package com.hawkeye.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiQueryItemVO {
    private String key;
    private String type;
    private String value;
    private Boolean required;
    private String description;
}
