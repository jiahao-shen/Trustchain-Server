package com.hawkeye.controller;

import com.alibaba.fastjson2.JSONObject;
import com.hawkeye.model.entity.User;
import com.hawkeye.model.enums.StatusCode;
import com.hawkeye.model.enums.UserRole;
import com.hawkeye.model.vo.BaseResponse;
import com.hawkeye.service.MinioService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private MinioService minioService;
    private static final Logger logger = LogManager.getLogger(TestController.class);

    @GetMapping("/hello")
    @ResponseBody
    public BaseResponse<String> hello() throws Exception {
        return new BaseResponse(StatusCode.SUCCESS, "", "hello world");
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<String> testGet(@PathVariable String id,
                                          @RequestParam String name) {
        logger.info("testGet");
        logger.info("id: " + id);
        logger.info("name: " + name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("age", "10")
                .header("sex", "Male")
                .body("Hello World");
    }

    @PostMapping("/post/x-www-form-urlencoded")
    public ResponseEntity<Object> testPostXwwwFormUrlencoded(@RequestParam String username,
                                                             @RequestParam String password,
                                                             HttpServletResponse response) {

        logger.info("testPostXWWWFormUrlEncoded");
        logger.info(username);
        logger.info(password);

        if (username.equals("plus")) {
            return ResponseEntity.status(HttpStatus.OK).body("12345");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new NullPointerException("fdsahjfhsdajklfhklsda"));
        }
    }

    @PostMapping("/post/raw/json")
    public ResponseEntity<Object> testPostRawJson(@RequestBody JSONObject request) {
        logger.info("testPostRawJson");
        logger.info(request);

        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("msg", "一切正常");
        User user = new User();
        user.setUsername("plus");
        user.setRole(UserRole.COMMON);
        user.setTelephone("13915558435");
        result.put("data", user);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/post/form-data")
    public ResponseEntity<Object> testPostRawFormData(@RequestParam String username,
                                                      @RequestPart MultipartFile logo) {

        logger.info("testPostFormData");
        logger.info("username: " + username);
        logger.info("logo size: " + logo.getSize());
        logger.info("logo name: " + logo.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/post/binary")
    public ResponseEntity<InputStreamResource> testPostBinary(HttpServletRequest request) throws IOException {
        ServletInputStream io = request.getInputStream();

        byte[] file = IOUtils.toByteArray(io);
        logger.info("file length: " + file.length);

        InputStream is = minioService.get("user/3e27cb04977eb070f8ef66aede92e2b9.jpg");
        InputStreamResource logo = new InputStreamResource(is);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(logo);
    }

}
