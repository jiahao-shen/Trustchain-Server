package com.trustchain.util;

import com.trustchain.exception.CaptchaException;
import com.trustchain.exception.NoPermissionException;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.vo.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = NoPermissionException.class)
    public ResponseEntity<Object> handleNoPermissionException(NoPermissionException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(value = CaptchaException.class)
    public ResponseEntity<Object> handleCaptchaException(CaptchaException e) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse(StatusCode.CAPTCHA_ERROR, "验证码不正确或已失效", null));
    }

}
