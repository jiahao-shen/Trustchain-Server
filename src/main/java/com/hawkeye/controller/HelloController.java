package com.hawkeye.controller;

import com.hawkeye.model.enums.StatusCode;
import com.hawkeye.model.vo.BaseResponse;
import com.hawkeye.service.MinioService;
import com.hawkeye.service.HelloService;
import com.hawkeye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private MinioService minioService;

    @Autowired
    private HelloService service;

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public BaseResponse test() throws Exception {
        return new BaseResponse(StatusCode.SUCCESS, "", "hello world");
    }
}
