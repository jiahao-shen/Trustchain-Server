package com.trustchain;

import com.alibaba.fastjson.JSON;
import com.trustchain.mapper.ApiInvokeLogMapper;
import com.trustchain.mapper.ApiMapper;
import com.trustchain.model.convert.ApiConvert;
import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiFormDataItem;
import com.trustchain.model.entity.ApiInvokeLog;
import com.trustchain.model.entity.ApiRequestBody;
import com.trustchain.model.vo.ApiFormDataItemVO;
import com.trustchain.model.vo.ApiRequestBodyVO;
import com.trustchain.model.vo.ApiVO;
import org.apache.commons.lang3.StringEscapeUtils;
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

    @Autowired
    private ApiInvokeLogMapper apiInvokeLogMapper;

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
    }

    @Test
    void testNestJSON() {
        ApiInvokeLog log = apiInvokeLogMapper.selectOneById("8244972f172e40e399d57037f217e6b6");
        String logJSON = JSON.toJSONString(log);
        logger.info(logJSON);
        logger.info(JSON.parseObject(StringEscapeUtils.unescapeJson(logJSON)));
//        String fuck = "[{\"count\":1,\"key\":\"R58N60MV001\",\"field\":\"certificate\",\"value\":\"{\\\"componentList\\\":[{\\\"componentId\\\":\\\"R2178472MX02\\\"},{\\\"componentId\\\":\\\"S34123214321X\\\"}],\\\"creationTime\\\":\\\"2024-05-19 15:14:49.445\\\",\\\"inspection\\\":[{\\\"standard\\\":\\\"Q/LGD001-2000\\\",\\\"inspector\\\":\\\"JC检03\\\",\\\"time\\\":\\\"2024-05-01 00:00:02\\\"},{\\\"standard\\\":\\\"GB6675.1-2014\\\",\\\"inspector\\\":\\\"JC检04\\\",\\\"time\\\":\\\"2024-05-06 00:32:00\\\"}],\\\"lastModified\\\":\\\"2024-05-19 15:14:49.445\\\",\\\"productionDate\\\":\\\"2024-05-15 00:00:00\\\",\\\"productionDepartment\\\":\\\"中国航发沈阳黎明航空发动机\\\",\\\"productionId\\\":\\\"R58N60MV001\\\",\\\"productionModel\\\":\\\"FWS-10发动机\\\",\\\"state\\\":\\\"已交付\\\",\\\"version\\\":\\\"cabebe7b477e47da8ea8dfd4a4708715\\\"}\",\"txId\":\"cabebe7b477e47da8ea8dfd4a4708715\",\"blockHeight\":\"u\",\"isDelete\":false,\"timestamp\":\"1716102889\"},{\"count\":2,\"key\":\"R58N60MV001\",\"field\":\"certificate\",\"value\":\"{\\\"componentList\\\":[{\\\"componentId\\\":\\\"R2178472MX02\\\"},{\\\"componentId\\\":\\\"S34123214321X\\\"}],\\\"creationTime\\\":\\\"2024-05-19 15:15:02.165\\\",\\\"inspection\\\":[{\\\"standard\\\":\\\"Q/LGD001-2000\\\",\\\"inspector\\\":\\\"JC检03\\\",\\\"time\\\":\\\"2024-05-01 00:00:02\\\"},{\\\"standard\\\":\\\"GB6675.1-2014\\\",\\\"inspector\\\":\\\"JC检04\\\",\\\"time\\\":\\\"2024-05-06 00:32:00\\\"}],\\\"lastModified\\\":\\\"2024-05-19 15:15:02.165\\\",\\\"productionDate\\\":\\\"2024-05-15 00:00:00\\\",\\\"productionDepartment\\\":\\\"中国航发沈阳黎明航空发动机\\\",\\\"productionId\\\":\\\"R58N60MV001\\\",\\\"productionModel\\\":\\\"FWS-10发动机\\\",\\\"state\\\":\\\"已交付\\\",\\\"version\\\":\\\"c5c71850b61746eca9b9746fceafc0cc\\\"}\",\"txId\":\"c5c71850b61746eca9b9746fceafc0cc\",\"blockHeight\":\"v\",\"isDelete\":false,\"timestamp\":\"1716102902\"},{\"count\":3,\"key\":\"R58N60MV001\",\"field\":\"certificate\",\"value\":\"{\\\"componentList\\\":[{\\\"componentId\\\":\\\"R2178472MX02\\\"},{\\\"componentId\\\":\\\"S34123214321X\\\"}],\\\"inspection\\\":[{\\\"standard\\\":\\\"Q/LGD001-2000\\\",\\\"inspector\\\":\\\"JC检03\\\",\\\"time\\\":\\\"2024-05-01 00:00:02\\\"},{\\\"standard\\\":\\\"GB6675.1-2014\\\",\\\"inspector\\\":\\\"JC检04\\\",\\\"time\\\":\\\"2024-05-06 00:32:00\\\"}],\\\"lastModified\\\":\\\"2024-05-19 16:02:34.27\\\",\\\"productionDate\\\":\\\"2024-05-15 00:00:00\\\",\\\"productionDepartment\\\":\\\"中国航发沈阳黎明航空发动机\\\",\\\"productionId\\\":\\\"R58N60MV001\\\",\\\"productionModel\\\":\\\"FWS-10发动机\\\",\\\"state\\\":\\\"已退回\\\",\\\"version\\\":\\\"18b2540854c04760bb036883ef5d6ab2\\\"}\",\"txId\":\"18b2540854c04760bb036883ef5d6ab2\",\"blockHeight\":\"\",\"isDelete\":false,\"timestamp\":\"1716105754\"},{\"count\":4,\"key\":\"R58N60MV001\",\"field\":\"certificate\",\"value\":\"{\\\"componentList\\\":[{\\\"componentId\\\":\\\"R2178472MX02\\\"},{\\\"componentId\\\":\\\"S34123214321X\\\"},{\\\"componentId\\\":\\\"S34BHDS23210\\\"}],\\\"inspection\\\":[{\\\"standard\\\":\\\"Q/LGD001-2000\\\",\\\"inspector\\\":\\\"JC检03\\\",\\\"time\\\":\\\"2024-05-01 00:00:02\\\"},{\\\"standard\\\":\\\"GB6675.1-2014\\\",\\\"inspector\\\":\\\"JC检04\\\",\\\"time\\\":\\\"2024-05-06 00:32:00\\\"}],\\\"lastModified\\\":\\\"2024-05-19 16:10:05.604\\\",\\\"productionDate\\\":\\\"2024-05-15 00:00:00\\\",\\\"productionDepartment\\\":\\\"中国航发沈阳黎明航空发动机\\\",\\\"productionId\\\":\\\"R58N60MV001\\\",\\\"productionModel\\\":\\\"FWS-10发动机\\\",\\\"state\\\":\\\"已退回\\\",\\\"version\\\":\\\"cc98f681befa47a5932b0c3100dd29dc\\\"}\",\"txId\":\"cc98f681befa47a5932b0c3100dd29dc\",\"blockHeight\":\"¯\",\"isDelete\":false,\"timestamp\":\"1716106205\"}]";
//        logger.info(StringEscapeUtils.unescapeJson(fuck));
//        logger.info(JSON.toJSONString(fuck, true));
    }
}
