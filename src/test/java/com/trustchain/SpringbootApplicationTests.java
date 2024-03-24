package com.trustchain;

import com.trustchain.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Test
    void testFilter() {
        List<User> fuck = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("plus");
        fuck.add(user1);
        User user2 = new User();
        user2.setUsername("good");
        fuck.add(user2);
        fuck.forEach(logger::info);

        User result = fuck.stream().filter(u -> u.getUsername().equals("plus")).findFirst().orElse(null);
        result.setUsername("hhhh");

        fuck.forEach(logger::info);
    }
}

