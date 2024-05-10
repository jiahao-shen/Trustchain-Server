package com.trustchain.controller;


import com.alibaba.fastjson2.JSONObject;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.service.CaptchaService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;
    private static final Logger logger = LogManager.getLogger(CaptchaController.class);

    @PostMapping("/send")
    @ResponseBody
    public BaseResponse<?> send(@RequestBody JSONObject request) {
        String email = request.getString("email");
        System.out.println("ttt"+email);
        boolean success = captchaService.send(email);

        if (success) {
            return new BaseResponse(StatusCode.SUCCESS, "", null);
        } else {
            return new BaseResponse<>(StatusCode.SEND_CAPTCHA_FAILED, "未知错误", null);
        }
    }
}
