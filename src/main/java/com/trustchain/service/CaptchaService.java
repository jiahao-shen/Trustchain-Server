package com.trustchain.service;

import com.trustchain.model.vo.BaseResponse;

public interface CaptchaService {
    /**
     * @param email 邮箱
     * @return 是否发送成功
     */
    boolean send(String email);

    /**
     * @param email 邮箱
     * @param code  验证码
     * @return 是否验证成功
     */
    boolean verify(String email, String code);
}
