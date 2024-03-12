package com.trustchain.service.impl;

import com.trustchain.service.HelloService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LogManager.getLogger(HelloServiceImpl.class);

    @Async
    public void testAsync() {
        try {
            Thread.sleep(10 * 1000);
            System.out.println("Hello World?");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public Future<Boolean> testAsyncResult() {
        try {
            Thread.sleep(10 * 1000);
            return new AsyncResult<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new AsyncResult<>(false);
        }
    }
}
