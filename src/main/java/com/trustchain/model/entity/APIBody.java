package com.trustchain.model.entity;

import com.trustchain.model.enums.BodyType;
import com.trustchain.model.enums.HttpBodyType;
import com.trustchain.model.enums.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.james.mime4j.stream.RawBody;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIBody {
    private HttpBodyType type;

    private List<APIFormDataItem> formDataBody;

    private List<APIXwwwFormUrlEncodedItem> xwwwFormUrlEncodedBody;

    private APIRawBody rawBody;

    private String binaryBody;

    private String graphQLBody;
}
