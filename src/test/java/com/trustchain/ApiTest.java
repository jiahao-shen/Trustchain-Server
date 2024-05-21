package com.trustchain;

import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ApiTest {
    private static final Logger logger = LogManager.getLogger(ApiTest.class);

    @Test
    void invokeTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("appKey", "f8110e10a4254c80a1a28d38f9ce3562");
        requestBody.put("secretKey", "e12089e0727941e6930ddacbdc45dfe2");
        requestBody.put("param", null);
        requestBody.put("query", null);
        requestBody.put("requestHeader", null);
        requestBody.put("requestBody", null);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8081/api/invoke/sdk")
                .post(RequestBody.create(MediaType.get("application/json"), requestBody.toJSONString()))
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                logger.info("Success");
                logger.info(response.body().string());
            } else {
                logger.error("failed");
                logger.error(response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
