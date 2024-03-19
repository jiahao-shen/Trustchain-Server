package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.trustchain.exception.NoPermissionException;
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

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", user));
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

    @PutMapping("/forgetPassword")
    public ResponseEntity<Object> forgetPassword(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String password = request.getString("password");
        String email = request.getString("email");
        String code = request.getString("code");

        User user = userService.findByUsername(orgId, username);

        if (user == null || !user.getEmail().equals(email)) {
            throw new NoPermissionException("用户不存在或邮箱错误");
        }

        // 判断验证码是否正确
        captchaService.verify(email, code);

        boolean success = userService.resetPassword(user, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "密码重置成功", success));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();
        String password = request.getString("password");
        String code = request.getString("code");

        // 判断验证码是否正确
        captchaService.verify(user.getEmail(), code);

        boolean success = userService.resetPassword(user, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "密码重置成功", success));
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

        // 判断验证码是否正确
        captchaService.verify(user.getEmail(), request.getString("code"));

        // 注册新用户
        boolean success = userService.register(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
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

        // 判断验证码是否正确
        captchaService.verify(userReg.getEmail(), request.getString("code"));

        String applyId = userService.registerApply(userReg);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", applyId));
    }

    @PostMapping("/register/apply/list")
    public ResponseEntity<Object> registerApplyList(@RequestBody JSONObject request) {
        List<String> applyIds = request.getList("applyIds", String.class);

        List<UserRegister> userRegs = userService.registerApplyList(applyIds);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVOList(userRegs)));
    }

    @PostMapping("/register/apply/detail")
    public ResponseEntity<Object> registerApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        UserRegister userReg = userService.registerApplyDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVO(userReg)));

    }

    @GetMapping("/register/approval/list")
    public ResponseEntity<Object> registerApprovalList() {
        User user = AuthUtil.getUser();
        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员用户无法查看用户注册审批列表");
        }

        List<UserRegister> userRegs = userService.registerList(user.getOrganizationId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVOList(userRegs)));
    }

    @PostMapping("/register/approval/detail")
    public ResponseEntity<Object> registerApprovalDetail(@RequestBody JSONObject request) {
        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员用户无法查看用户注册审批详情");
        }

        String applyId = request.getString("applyId");
        UserRegister userReg = userService.registerDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        UserConvert.INSTANCE.toUserRegisterVO(userReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员用户无法审批用户注册申请");
        }

        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean success = userService.registerReply(applyId, reply, reason);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @GetMapping("/subordinate/list")
    public ResponseEntity<Object> subordindateList() {
        User user = AuthUtil.getUser();
        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员用户无法查看用户列表");
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
        User user = AuthUtil.getUser();

        user.setLogo(request.getString("logo"));
        user.setUsername(request.getString("username"));
        user.setTelephone(request.getString("telephone"));
        user.setEmail(request.getString("email"));

        // 判断验证码是否正确
        captchaService.verify(user.getEmail(), request.getString("code"));

        User updateUser = userService.informationUpdate(user);
        AuthUtil.setUser(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVO(updateUser)));
    }

    @PostMapping("/information/history")
    public ResponseEntity<Object> informationHistory(@RequestBody JSONObject request) {
        String userId = request.getString("userId");

        if (!AuthUtil.getUser().getId().equals(userId)) {
            throw new NoPermissionException("非本用户无法查看用户信息历史记录");
        }

        List<User> users = userService.informationHistory(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVOList(users)));
    }

    @PostMapping("/information/rollback")
    public ResponseEntity<Object> informationRollback(@RequestBody JSONObject request) {
        String userId = request.getString("userId");
        String version = request.getString("version");

        if (!AuthUtil.getUser().getId().equals(userId)) {
            throw new NoPermissionException("非本用户无法回滚用户信息");
        }

        boolean success = userService.informationRollback(userId, version);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

}
