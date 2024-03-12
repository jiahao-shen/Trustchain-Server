package com.trustchain.service;

public interface CaptchaService {
    /**
     * @param email
     * @return
     */
    boolean send(String email);

    /**
     * @param email
     * @param code
     * @return
     */
    boolean verify(String email, String code);
}
