package com.hawkeye.service;


import java.util.concurrent.Future;

public interface HelloService {
    void testAsync();

    Future<Boolean> testAsyncResult();
}
