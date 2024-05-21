package com.hawkeye.service;

public interface CaptchaService {
    /**
     * @param email 邮箱
     * @return 是否发送成功
     */
    boolean send(String email);

    /**
     * @param email 邮箱
     * @param code  验证码
     */
    void verify(String email, String code);
}
