package com.trustchain.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS(1000),                          // 成功
    LOGIN_FAILED(1001),                     // 登录失败
    RESET_PASSWORD_FAILED(1002),            // 重置密码失败
    UPLOAD_FILE_FAILED(1003),               // 文件上传失败
    REGISTER_FAILED(1004),                  // 注册失败
    SEND_CAPTCHA_FAILED(1005),              // 发送验证码失败
    CAPTCHA_ERROR(1006);                    // 验证码错误

    @EnumValue
    private final int code;
}
