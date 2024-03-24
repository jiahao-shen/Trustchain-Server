package com.trustchain.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.HttpRequestBodyType;
import com.trustchain.model.serializer.MinioURLSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestBody {
    private HttpRequestBodyType type;

    private List<ApiFormDataItem> formDataBody;

    private List<ApiXwwwFormUrlEncodedItem> xwwwFormUrlEncodedBody;

    private ApiRawBody rawBody;

    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String binaryBody;

    private String graphQLBody;
}
