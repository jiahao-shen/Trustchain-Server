package com.trustchain.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.serializer.StatusCodeSerializer;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    @JSONField(serializeUsing = StatusCodeSerializer.class)
    private StatusCode code;

    private String message;

    private T data;
}

