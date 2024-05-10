package com.trustchain;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.TypeReference;
import com.trustchain.service.ChainService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainClientException;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class ChainTest {
    @Autowired
    private ChainService chainService;

    private static final Logger logger = LogManager.getLogger(ChainTest.class);

    @Test
    void testUpdate() {
        ResultOuterClass.ContractResult res = chainService.putState("123456", "test", "good");
        logger.info(res);
    }

    @Test
    void testHistory() {
      String res = chainService.getHistory("e675a62fa8f24e9ebc0cff4e1a1634c5", "organization");
      logger.info(res);
//      String fuck = new String(res.toByteArray(), StandardCharsets.UTF_8);
//      logger.info(fuck);
    }

    @Test
    void testGet() {

    }

    @Test
    void testVerion() {

    }
}