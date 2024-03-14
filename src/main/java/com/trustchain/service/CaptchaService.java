package com.trustchain.service;

import com.trustchain.model.vo.BaseResponse;

public interface CaptchaService {
    /**
     * @param email: 邮箱
     * @return:
     */
    boolean send(String email);

    boolean verify(String email, String code);
}
