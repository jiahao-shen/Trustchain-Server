package com.trustchain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringbootApplicationTests {
    private static final Logger logger = LogManager.getLogger(SpringbootApplicationTests.class);

    @Test
    void testDate() {
        logger.info(new Date());
        logger.info(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(100)));
    }

    @Test
    void testReg() {
        String temp = "百行";
        logger.info(temp.replaceAll("(?<=.)(?=.)", "%"));
    }
}

