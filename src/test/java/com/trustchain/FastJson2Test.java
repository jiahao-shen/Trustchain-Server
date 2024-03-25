package com.trustchain;

import com.alibaba.fastjson2.JSON;
import com.trustchain.mapper.ApiMapper;
import com.trustchain.model.convert.ApiConvert;
import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiFormDataItem;
import com.trustchain.model.entity.ApiRequestBody;
import com.trustchain.model.vo.ApiFormDataItemVO;
import com.trustchain.model.vo.ApiRequestBodyVO;
import com.trustchain.model.vo.ApiVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@SpringBootTest
public class FastJson2Test {
    @Autowired
    private ApiMapper apiMapper;

    private static final Logger logger = LogManager.getLogger(FastJson2Test.class);

    @Test
    void testFastJson2() {
        String requestBody = "{\"type\":\"FORM_DATA\",\"formDataBody\":[{\"key\":\"age\",\"type\":\"Int\",\"required\":true,\"description\":\"年龄\"}],\"xwwwFormUrlEncodedBody\":[],\"rawBody\":{\"type\":\"JSON\",\"body\":\"\"},\"binaryBody\":\"\",\"graphQLBody\":\"\"}";
        ApiRequestBody body = JSON.parseObject(requestBody, ApiRequestBody.class);
        logger.info(body);
//        UserRegisterVO user = new UserRegisterVO();
//        logger.info(JSON.toJSONString(user));
    }

    @Test
    void testObjectWriter() {
        ApiFormDataItemVO api = new ApiFormDataItemVO("name", "String", "plus", true, "年龄");

        logger.info(JSON.toJSONString(api));
    }

    @Test
    void testFormDataSerializer() {
        Api api = apiMapper.selectOneById("647c1f249d4d44d8818ced44b1e16611");
        ApiVO vo = ApiConvert.INSTANCE.toApiVO(api);

        logger.info(JSON.toJSONString(vo));
    }
}
