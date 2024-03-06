package com.trustchain.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSONObject;
import com.trustchain.convert.UserConvert;
import com.trustchain.enums.StatusCode;
import com.trustchain.model.entity.User;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.UserInformationVO;
import com.trustchain.model.vo.UserLoginVO;
import com.trustchain.service.UserService;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        String organization = request.getString("organization");
        String username = request.getString("username");
        String password = request.getString("password");

        UserLoginVO user = userService.login(organization, username, password);

        if (user != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS,
                            "登录成功", user));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.LOGIN_FAILED, "用户名或密码错误", null));
        }
    }

    /**
     * 用户退出登录
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        String id = request.getString("id");
        userService.logout(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "退出登录成功", null));
    }
//
//    /**
//     * 用户注册
//     */
//    @PostMapping("/user/register")
//    public ResponseEntity<Object> userRegister(@RequestBody JSONObject request, HttpSession session) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        User user = new User();
//        user.setUsername(request.getString("username"));
//        user.setPassword(encoder.encode(request.getString("password")));
//        user.setOrganization(Long.parseLong(request.getString("organization")));
//        user.setType(UserType.valueOf(request.getString("type")));
//        user.setCreatedTime(new Date());
//
//        int count = userMapper.insert(user);
//        if (count != 0) {
//            return ResponseEntity.status(HttpStatus.OK).body(true);
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(false);
//        }
//    }
//
//    /**
//     * 获取指定机构下的用户列表
//     */
//    @GetMapping("/user/list")
//    public ResponseEntity<Object> userList(HttpSession session) {
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
//        }
//
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.select(User::getId, User::getUsername, User::getType, User::getCreatedTime)
//                .eq(User::getOrganization, login.getOrganization());
//
//        List<User> userList = userMapper.selectList(queryWrapper);
//
//        return ResponseEntity.status(HttpStatus.OK).body(userList);
//    }
//
//    /**
//     * 判断用户是否存在
//     */
//    @PostMapping("/user/exist")
//    public ResponseEntity<Object> userExist(@RequestBody JSONObject request, HttpSession session) {
//        logger.info(request);
//
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getUsername, request.getString("username"));
//
//        User user = userMapper.selectOne(queryWrapper);
//
//        if (user != null) {
//            return ResponseEntity.status(HttpStatus.OK).body(true);
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(false);
//        }
//    }
}
