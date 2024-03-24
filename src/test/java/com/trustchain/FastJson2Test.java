package com.trustchain;

import com.alibaba.fastjson2.JSON;
import com.trustchain.model.dto.ApiRequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FastJson2Test {

    private static final Logger logger = LogManager.getLogger(FastJson2Test.class);

    @Test
    void testFastJson2() {
        String requestBody = "{\"type\":\"FORM_DATA\",\"formDataBody\":[{\"key\":\"age\",\"type\":\"Int\",\"required\":true,\"description\":\"年龄\"}],\"xwwwFormUrlEncodedBody\":[],\"rawBody\":{\"type\":\"JSON\",\"body\":\"\"},\"binaryBody\":\"\",\"graphQLBody\":\"\"}";
        ApiRequestBody body = JSON.parseObject(requestBody, ApiRequestBody.class);
        logger.info(body);
//        UserRegisterVO user = new UserRegisterVO();
//        logger.info(JSON.toJSONString(user));
    }
}
