package com.trustchain.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.HttpResponseBodyType;
import com.trustchain.model.serializer.MinioURLSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseBody {
    private HttpResponseBodyType type;

    private ApiRawBody rawBody;

    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String binaryBody;

    private String graphQLBody;
}
