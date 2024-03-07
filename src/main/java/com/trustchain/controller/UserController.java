package com.trustchain.controller;

import com.alibaba.fastjson.JSONObject;
import com.trustchain.convert.OrganizationConvert;
import com.trustchain.convert.UserConvert;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.enums.StatusCode;
import com.trustchain.enums.UserRole;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.UserLogin;
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
import java.util.List;

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
    public ResponseEntity<Object> login(@RequestBody JSONObject request) {
        logger.info(request);

        String organization = request.getString("organization");
        String username = request.getString("username");
        String password = request.getString("password");

        UserLogin user = userService.login(organization, username, password);

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
    public ResponseEntity<Object> logout(@RequestBody JSONObject request) {
        String id = request.getString("id");
        userService.logout(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "退出登录成功", null));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody JSONObject request) {
        logger.info(request);

        User user = new User();
        user.setUsername(request.getString("username"));
        user.setPassword(PasswordUtil.encrypt(request.getString("password")));
        user.setOrganizationID(request.getString("organization"));
        user.setTelephone(request.getString("telephone"));
        user.setEmail(request.getString("email"));
        user.setRole(UserRole.valueOf(request.getString("role")));

        // TODO: 验证码
//        String verificationCode = request.getString("verificationCode");

        Boolean flag = userService.register(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "注册成功", flag));
    }

    @PostMapping("/register/apply")
    public ResponseEntity<Object> registerApply(@RequestBody JSONObject request) {
        UserRegister userReg = new UserRegister();
        userReg.setLogo(request.getString("logo"));
        userReg.setOrganizationID(request.getString("organization"));
        userReg.setUsername(request.getString("username"));
        userReg.setPassword(PasswordUtil.encrypt(request.getString("password")));
        userReg.setTelephone(request.getString("telephone"));
        userReg.setEmail(request.getString("email"));
        userReg.setRole(UserRole.valueOf(request.getString("role")));
        userReg.setRegStatus(RegisterStatus.PENDING);

        // TODO: 验证邮箱
        String verificationCode = request.getString("verificationCode");

        String regID = userService.registerApply(userReg);

        if (regID != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "注册申请成功", regID));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.REGISTER_FAILED, "注册申请失败", null));
        }
    }

    @PostMapping("/exist")
    public ResponseEntity<Object> exist(@RequestBody JSONObject request) {
        String organization = request.getString("organization");
        String username = request.getString("username");
        String id = request.getString("id");

        Boolean result = userService.exist(organization, username, id);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

    @PostMapping("/register/apply/search")
    public ResponseEntity<Object> registerApplySearch(@RequestBody JSONObject request) {
        List<String> regIDs = request.getJSONArray("regIDs");

        List<UserRegister> userRegs = userService.registerApplySearch(regIDs);

//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new BaseResponse<>(StatusCode.SUCCESS, "", UserConvert.INSTANCE.to))
        return null;
    }
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
