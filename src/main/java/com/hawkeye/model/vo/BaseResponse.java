package com.hawkeye.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.StatusCode;
import com.hawkeye.model.serializer.StatusCodeSerializer;
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

