package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.trustchain.model.convert.UserConvert;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.enums.UserRole;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.UserLogin;
import com.trustchain.model.vo.UserVO;
import com.trustchain.service.CaptchaService;
import com.trustchain.service.UserService;
import com.trustchain.util.AuthUtil;
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

    @Autowired
    private CaptchaService captchaService;

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody JSONObject request) {
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

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody JSONObject request) {
        String userId = request.getString("userId");
        userService.logout(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "退出登录成功", null));
    }


    @PostMapping("/exist")
    public ResponseEntity<Object> exist(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String userId = request.getString("userId");

        boolean result = userService.exist(orgId, username, userId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String password = request.getString("password");

        String email = request.getString("email");
        String code = request.getString("code");

        // 判断用户是否存在以及邮箱是否正确
        User user = userService.findByUsername(orgId, username);
        if (user == null || !user.getEmail().equals(email)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.RESET_PASSWORD_FAILED, "用户名或邮箱错误", null));
        }

        // 判断验证码是否正确
        if (!captchaService.verify(email, code)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.CAPTCHA_ERROR, "验证码不正确或已失效", null));
        }

        // 重置密码
        if (userService.resetPassword(user, password)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "密码重置成功", null));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.RESET_PASSWORD_FAILED, "未知错误", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody JSONObject request) {
        User user = new User();
        user.setOrganizationId(request.getString("orgId"));
        user.setUsername(request.getString("username"));
        user.setPassword(PasswordUtil.encrypt(request.getString("password")));
        user.setTelephone(request.getString("telephone"));
        user.setEmail(request.getString("email"));
        user.setRole(UserRole.valueOf(request.getString("role")));

        String code = request.getString("code");
        // 判断验证码是否正确
        if (!captchaService.verify(user.getEmail(), code)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.CAPTCHA_ERROR, "验证码不正确或已失效", null));
        }

        // 注册新用户
        if (userService.register(user)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "注册成功", null));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.REGISTER_FAILED, "未知错误", null));
        }
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

        String code = request.getString("code");
        // 判断验证码是否正确
        if (!captchaService.verify(userReg.getEmail(), code)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.CAPTCHA_ERROR, "验证码不正确或已失效", null));
        }

        String applyId = userService.registerApply(userReg);

        if (applyId != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "注册申请成功", applyId));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.REGISTER_FAILED, "未知错误", null));
        }
    }

    @PostMapping("/register/apply/search")
    public ResponseEntity<Object> registerApplySearch(@RequestBody JSONObject request) {
        List<String> applyIds = request.getList("applyIds", String.class);

        List<UserRegister> userRegs = userService.registerApplySearch(applyIds);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVOList(userRegs)));
    }

    @GetMapping("/register/list")
    public ResponseEntity<Object> registerList() {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        List<UserRegister> userRegs = userService.registerList(user.getOrganizationId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVOList(userRegs)));
    }

    @PostMapping("/register/detail")
    public ResponseEntity<Object> registerDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        UserRegister userReg = userService.registerDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVO(userReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean result = userService.registerReply(applyId, reply, reason);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

    @GetMapping("/subordinate/list")
    public ResponseEntity<Object> subordindateList() {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        List<User> users = userService.subordinateList(user.getOrganizationId());

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

    @PutMapping("/information/update")
    public ResponseEntity<Object> informationUpdate(@RequestBody JSONObject request) {
        User user = new User();
        user.setId(request.getString("userId"));
        user.setLogo(request.getString("logo"));
        user.setUsername(request.getString("username"));
        user.setTelephone(request.getString("telephone"));
        user.setEmail(request.getString("email"));

        String code = request.getString("code");

        // 判断验证码是否正确
        if (!captchaService.verify(user.getEmail(), code)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.CAPTCHA_ERROR, "验证码不正确或已失效", null));
        }

        User updateUser = userService.informationUpdate(user);
        AuthUtil.setUser(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVO(updateUser)));
    }

    @PostMapping("/information/history")
    public ResponseEntity<Object> informationHistory(@RequestBody JSONObject request) {
        return null;
    }

    @PostMapping("/information/rollback")
    public ResponseEntity<Object> informationRollback(@RequestBody JSONObject request) {
        return null;
    }

}
