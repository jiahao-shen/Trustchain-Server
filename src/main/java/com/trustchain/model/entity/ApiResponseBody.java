package com.trustchain.model.entity;

import com.trustchain.model.enums.HttpResponseBodyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseBody {
    private HttpResponseBodyType type;

    private ApiRawBody rawBody;

    private String binaryBody;

    private String graphQLBody;
}
