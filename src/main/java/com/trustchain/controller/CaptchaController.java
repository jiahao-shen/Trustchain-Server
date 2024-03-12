package com.trustchain.controller;


import com.alibaba.fastjson.JSONObject;
import com.trustchain.enums.StatusCode;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.service.CaptchaService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    private static final Logger logger = LogManager.getLogger(CaptchaController.class);

    @PostMapping("/send")
    public ResponseEntity<Object> send(@RequestBody JSONObject request) {
        String email = request.getString("email");

        boolean flag = captchaService.send(email);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", flag));
    }


    @PostMapping("/verify")
    public ResponseEntity<Object> verify(@RequestBody JSONObject request) {
        String email = request.getString("email");
        String code = request.getString("code");

        boolean flag = captchaService.verify(email, code);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", flag));
    }
}
