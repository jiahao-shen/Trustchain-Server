package com.trustchain.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.serializer.FormDataSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiFormDataItemVO {

    private String key;

    private String type;

    private String value;

    private Boolean required;

    private String description;
}
