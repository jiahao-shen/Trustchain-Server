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
import com.trustchain.model.vo.UserRegisterVO;
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
    @ResponseBody
    public BaseResponse<UserLogin> login(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String password = request.getString("password");

        UserLogin user = userService.login(orgId, username, password);

        return new BaseResponse(StatusCode.SUCCESS, "", user);
    }

    @PostMapping("/logout")
    @ResponseBody
    public BaseResponse<?> logout(@RequestBody JSONObject request) {
        String userId = request.getString("userId");
        userService.logout(userId);

        return new BaseResponse(StatusCode.SUCCESS, "退出登录成功", null);
    }


    @PostMapping("/exist")
    @ResponseBody
    public BaseResponse<Boolean> exist(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String username = request.getString("username");
        String userId = request.getString("userId");

        boolean result = userService.exist(orgId, username, userId);

        return new BaseResponse(StatusCode.SUCCESS, "", result);
    }

    @PutMapping("/forgetPassword")
    @ResponseBody
    public BaseResponse<Boolean> forgetPassword(@RequestBody JSONObject request) {
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

        return new BaseResponse(StatusCode.SUCCESS, "密码重置成功", success);
    }

    @PutMapping("/resetPassword")
    @ResponseBody
    public BaseResponse<Boolean> resetPassword(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();
        String password = request.getString("password");
        String code = request.getString("code");

        // 判断验证码是否正确
        captchaService.verify(user.getEmail(), code);

        boolean success = userService.resetPassword(user, password);

        return new BaseResponse<>(StatusCode.SUCCESS, "密码重置成功", success);
    }

    @PostMapping("/register")
    @ResponseBody
    public BaseResponse<Boolean> register(@RequestBody JSONObject request) {
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

        return new BaseResponse<>(StatusCode.SUCCESS, "", success);
    }

    @PostMapping("/register/apply")
    @ResponseBody
    public BaseResponse<String> registerApply(@RequestBody JSONObject request) {
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

        return new BaseResponse<>(StatusCode.SUCCESS, "", applyId);
    }

    @PostMapping("/register/apply/list")
    @ResponseBody
    public BaseResponse<List<UserRegisterVO>> registerApplyList(@RequestBody JSONObject request) {
        List<String> applyIds = request.getList("applyIds", String.class);

        List<UserRegister> userRegs = userService.registerApplyList(applyIds);

        return new BaseResponse(StatusCode.SUCCESS, "",
                UserConvert.INSTANCE.toUserRegisterVOList(userRegs));
    }

    @PostMapping("/register/apply/detail")
    @ResponseBody
    public BaseResponse<UserRegisterVO> registerApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        UserRegister userReg = userService.registerApplyDetail(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                UserConvert.INSTANCE.toUserRegisterVO(userReg));

    }

    @GetMapping("/register/approval/list")
    @ResponseBody
    public BaseResponse<List<UserRegisterVO>> registerApprovalList() {
        User user = AuthUtil.getUser();
        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员用户无法查看用户注册审批列表");
        }

        List<UserRegister> userRegs = userService.registerList(user.getOrganizationId());

        return new BaseResponse(StatusCode.SUCCESS, "",
                UserConvert.INSTANCE.toUserRegisterVOList(userRegs));
    }

    @PostMapping("/register/approval/detail")
    @ResponseBody
    public BaseResponse<UserRegisterVO> registerApprovalDetail(@RequestBody JSONObject request) {
        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员用户无法查看用户注册审批详情");
        }

        String applyId = request.getString("applyId");
        UserRegister userReg = userService.registerDetail(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                UserConvert.INSTANCE.toUserRegisterVO(userReg));
    }

    @PostMapping("/register/reply")
    @ResponseBody
    public BaseResponse<Boolean> registerReply(@RequestBody JSONObject request) {
        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员用户无法审批用户注册申请");
        }

        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean success = userService.registerReply(applyId, reply, reason);

        return new BaseResponse(StatusCode.SUCCESS, "", success);
    }

    @GetMapping("/subordinate/list")
    @ResponseBody
    public BaseResponse<List<UserVO>> subordindateList() {
        User user = AuthUtil.getUser();
        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员用户无法查看用户列表");
        }

        List<User> users = userService.subordinateList(user.getOrganizationId());

        return new BaseResponse(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVOList(users));
    }

    @PostMapping("/subordinate/detail")
    @ResponseBody
    public BaseResponse<UserVO> subordinateDetail(@RequestBody JSONObject request) {
        String userId = request.getString("userId");

        User user = userService.subordinateDetail(userId);

        return new BaseResponse(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVO(user));
    }

    @PostMapping("/information/detail")
    @ResponseBody
    public BaseResponse<UserVO> informationDetail(@RequestBody JSONObject request) {
        String userId = request.getString("userId");
        String version = request.getString("version");

        User user = userService.informationDetail(userId, version);
        UserVO userInfo = UserConvert.INSTANCE.toUserVO(user);
        userInfo.setLatest(true);

        return new BaseResponse(StatusCode.SUCCESS, "", userInfo);
    }

    @PutMapping("/information/update")
    @ResponseBody
    public BaseResponse<UserVO> informationUpdate(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        user.setLogo(request.getString("logo"));
        user.setUsername(request.getString("username"));
        user.setTelephone(request.getString("telephone"));
        user.setEmail(request.getString("email"));

        // 判断验证码是否正确
        captchaService.verify(user.getEmail(), request.getString("code"));

        User updateUser = userService.informationUpdate(user);
        AuthUtil.setUser(user);

        return new BaseResponse(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVO(updateUser));
    }

    @PostMapping("/information/history")
    @ResponseBody
    public BaseResponse<List<UserVO>> informationHistory(@RequestBody JSONObject request) {
        String userId = request.getString("userId");

        if (!AuthUtil.getUser().getId().equals(userId)) {
            throw new NoPermissionException("非本用户无法查看用户信息历史记录");
        }

        List<User> users = userService.informationHistory(userId);

        return new BaseResponse(StatusCode.SUCCESS, "", UserConvert.INSTANCE.toUserVOList(users));
    }

    @PostMapping("/information/rollback")
    @ResponseBody
    public BaseResponse<Boolean> informationRollback(@RequestBody JSONObject request) {
        String userId = request.getString("userId");
        String version = request.getString("version");

        if (!AuthUtil.getUser().getId().equals(userId)) {
            throw new NoPermissionException("非本用户无法回滚用户信息");
        }

        boolean success = userService.informationRollback(userId, version);

        return new BaseResponse(StatusCode.SUCCESS, "", success);
    }

}
