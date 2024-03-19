package com.trustchain.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS(1000),                          // 成功
    UPLOAD_FILE_FAILED(1003),               // 文件上传失败
    SEND_CAPTCHA_FAILED(1005),              // 发送验证码失败
    CAPTCHA_ERROR(1006);                    // 验证码错误

    @EnumValue
    private final int code;
}
