package com.trustchain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiXwwwFormUrlEncodedItemVO {
    private String key;
    private String type;
    private String value;
    private Boolean required;
    private String description;
}
