package com.trustchain.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.HttpRequestBodyType;
import com.trustchain.model.serializer.FormDataSerializer;
import com.trustchain.model.serializer.MinioUrlSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestBodyVO {
    private HttpRequestBodyType type;

    @JSONField(serializeUsing = FormDataSerializer.class)
    private List<ApiFormDataItemVO> formDataBody;

    private List<ApiXwwwFormUrlEncodedItemVO> xwwwFormUrlEncodedBody;

    private ApiRawBodyVO rawBody;

    @JSONField(serializeUsing = MinioUrlSerializer.class)
    private String binaryBody;

    private String graphQLBody;
}
