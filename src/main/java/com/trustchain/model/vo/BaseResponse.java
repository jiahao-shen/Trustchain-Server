package com.trustchain.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.trustchain.enums.StatusCode;
import com.trustchain.enums.StatusCodeSerializer;
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
