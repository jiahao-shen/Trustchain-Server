package com.trustchain.model.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiFormDataItem {

    private String key;

    private String type;

    private String value;

    private Boolean required;

    private String description;
}
