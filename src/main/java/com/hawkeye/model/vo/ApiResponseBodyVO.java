package com.hawkeye.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.HttpResponseBodyType;
import com.hawkeye.model.serializer.MinioUrlSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseBodyVO {
    private HttpResponseBodyType type;

    private ApiRawBodyVO rawBody;

    @JSONField(serializeUsing = MinioUrlSerializer.class)
    private String binaryBody;

    private String graphQLBody;
}
