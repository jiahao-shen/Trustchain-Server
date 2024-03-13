package com.trustchain.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trustchain.enums.OrganizationType;
import com.trustchain.model.convert.UserConvert;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.enums.StatusCode;
import com.trustchain.enums.UserRole;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.UserLogin;
import com.trustchain.model.vo.UserVO;
import com.trustchain.service.UserService;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String password = request.getString("password");

        UserLogin user = userService.login(orgId, username, password);

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
        String userId = request.getString("userId");
        userService.logout(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "退出登录成功", null));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String password = request.getString("password");

        boolean flag = userService.resetPassword(orgId, username, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", flag));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody JSONObject request) {
        logger.info(request);

        User user = new User();
        user.setUsername(request.getString("username"));
        user.setPassword(PasswordUtil.encrypt(request.getString("password")));
        user.setOrganizationId(request.getString("orgId"));
        user.setTelephone(request.getString("telephone"));
        user.setEmail(request.getString("email"));
        user.setRole(UserRole.valueOf(request.getString("role")));

        boolean flag = userService.register(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "注册成功", flag));
    }

    @PostMapping("/register/apply")
    public ResponseEntity<Object> registerApply(@RequestBody JSONObject request) {
        UserRegister userReg = new UserRegister();
        userReg.setLogo(request.getString("logo"));
        userReg.setOrganizationId(request.getString("orgId"));
        userReg.setUsername(request.getString("username"));
        userReg.setPassword(PasswordUtil.encrypt(request.getString("password")));
        userReg.setTelephone(request.getString("telephone"));
        userReg.setEmail(request.getString("email"));
        userReg.setRole(UserRole.valueOf(request.getString("role")));
        userReg.setRegStatus(RegisterStatus.PENDING);

        String regId = userService.registerApply(userReg);

        if (regId != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "注册申请成功", regId));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.REGISTER_FAILED, "注册申请失败", null));
        }
    }

    @PostMapping("/register/apply/search")
    public ResponseEntity<Object> registerApplySearch(@RequestBody JSONObject request) {
        System.out.println(request);
        List<String> regIds = request.getJSONArray("regIds");

        List<UserRegister> userRegs = userService.registerApplySearch(regIds);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserRegisterVOList(userRegs)));
    }

    @PostMapping("/exist")
    public ResponseEntity<Object> exist(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String userId = request.getString("userId");

        boolean result = userService.exist(orgId, username, userId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

    @PostMapping("/register/list")
    public ResponseEntity<Object> registerList(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        List<UserRegister> userRegs = userService.registerList(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVOList(userRegs)));
    }

    @PostMapping("/register/detail")
    public ResponseEntity<Object> registerDetail(@RequestBody JSONObject request) {
        String regId = request.getString("regId");

        UserRegister userReg = userService.registerDetail(regId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVO(userReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        String regId = request.getString("regId");
        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean flag = userService.registerReply(regId, reply, reason);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", flag));
    }

    @PostMapping("/subordinate/list")
    public ResponseEntity<Object> subordindateList(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        List<User> users = userService.subordinateList(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVOList(users)));
    }

    @PostMapping("/subordinate/detail")
    public ResponseEntity<Object> subordinateDetail(@RequestBody JSONObject request) {
        String userId = request.getString("userId");

        User user = userService.subordinateDetail(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVO(user)));
    }

    @PostMapping("/information/detail")
    public ResponseEntity<Object> informationDetail(@RequestBody JSONObject request) {
        String userId = request.getString("userId");
        String version = request.getString("version");

        User user = userService.informationDetail(userId, version);
        UserVO userInfo = UserConvert.INSTANCE.toUserVO(user);
        userInfo.setLatest(true);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", userInfo));
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
}
