package com.trustchain.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.trustchain.SpringbootApplication;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {
    private static final Logger logger = LogManager.getLogger(TestController.class);

    @GetMapping("/get/id/{id}")
    public ResponseEntity<Object> testGet(@PathVariable String id,
                                          @RequestParam String name) {
        logger.info("testGet");
        logger.info("id: " + id);
        logger.info("name: " + name);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/post/x-www-form-urlencoded")
    public ResponseEntity<Object> testPostXwwwFormUrlencoded(@RequestParam String username,
                                                             @RequestParam String password) {

        logger.info("testPostXWWWFormUrlEncoded");
        logger.info(username);
        logger.info(password);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/post/raw/json")
    public ResponseEntity<Object> testPostRawJson(@RequestBody JSONObject request) {
        logger.info("testPostRawJson");
        logger.info(request);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/post/form-data")
    public ResponseEntity<Object> testPostRawFormData(@RequestParam String username,
                                                      @RequestPart MultipartFile logo) {

        logger.info("testPostFormData");
        logger.info("username: " + username);
        logger.info("logo size: " + logo.getSize());
        logger.info("logo name: "  + logo.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/post/binary")
    public ResponseEntity<Object> testPostBinary(HttpServletRequest request) throws IOException {
        ServletInputStream io = request.getInputStream();

        byte[] file = IOUtils.toByteArray(io);

        logger.info("file length: " + file.length);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
