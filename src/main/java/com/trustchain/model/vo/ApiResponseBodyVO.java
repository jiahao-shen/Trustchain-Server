package com.trustchain.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.HttpResponseBodyType;
import com.trustchain.model.serializer.MinioUrlSerializer;
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
