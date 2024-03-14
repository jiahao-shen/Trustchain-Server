package com.trustchain.controller;

import com.trustchain.service.MinioService;
import com.trustchain.service.HelloService;
import com.trustchain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.trustchain.model.entity.User;

import java.util.List;

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
    public List<User> test() throws Exception {
        List<User> users = userService.subordinateList("a0c7b2b7e3564e70b6f4f80ef42b9b24");

        return users;
//        service.testAsync();
//        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "good", null));

    }

    @GetMapping("/fuck")
    public ResponseEntity<Object> fuck() throws Exception{
        List<User> users = userService.subordinateList("a0c7b2b7e3564e70b6f4f80ef42b9b24");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
    }
//
//    @PostMapping("/test")
//    public String test(@RequestBody JSONObject request) {
////        System.out.println(request.getString("name"));
////        JSONArray users = request.getJSONArray("users");
////        System.out.println(users);
////        Integer age = request.get("age");
//
//        return "Test";
//    }
//
//    @GetMapping("/fuck")
//    public ResponseEntity<Object> fuck() {
//        OrganizationRegister ro = organizationRegisterMapper.selectById("1583321439534010370");
//
//        return ResponseEntity.status(HttpStatus.OK).body(ro);
//    }
//
//    @GetMapping("/test/fabric")
//    public ResponseEntity<Object> testFabric() {
//        FabricGateway fg = new FabricGateway();
//        return ResponseEntity.status(HttpStatus.OK).body(fg.query("queryAPIList"));
//    }
//
//    @PostMapping("/test/minio")
//    public ResponseEntity<Object> testMinio(@RequestPart("file") MultipartFile file) {
//        try {
//            minioService.upload(file, "test.jpg");
//            return ResponseEntity.status(HttpStatus.OK).body("good");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.OK).body("no");
//        }
//    }
}
