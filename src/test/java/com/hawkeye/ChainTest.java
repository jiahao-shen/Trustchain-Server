package com.hawkeye;

import com.hawkeye.service.ChainService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChainTest {
    @Autowired
    private ChainService chainService;

    private static final Logger logger = LogManager.getLogger(ChainTest.class);

    @Test
    void testUpdate() {
        String txId = chainService.putState("123456", "test", "good");
        logger.info(txId);
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
        String res = chainService.getState("d74dfc93e4974f369f5aea9311fd41b9", "api");
        logger.info(res);
    }

    @Test
    void testVerion() {

    }
}