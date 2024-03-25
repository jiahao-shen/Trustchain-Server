package com.trustchain.model.entity;

import com.trustchain.model.enums.HttpRequestBodyType;
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

    private String binaryBody;

    private String graphQLBody;
}
