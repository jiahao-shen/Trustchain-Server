package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.trustchain.exception.NoPermissionException;
import com.trustchain.model.convert.ApiConvert;
import com.trustchain.model.dto.ApiBody;
import com.trustchain.model.dto.ApiHeaderItem;
import com.trustchain.model.dto.ApiParamItem;
import com.trustchain.model.dto.ApiQueryItem;
import com.trustchain.model.entity.*;
import com.trustchain.model.enums.*;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.service.ApiService;
import com.trustchain.service.CaptchaService;
import com.trustchain.util.AuthUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ApiService apiService;

    @Autowired
    private CaptchaService captchaService;

    private static final Logger logger = LogManager.getLogger(ApiController.class);

    @PostMapping("/register/apply")
    public ResponseEntity<Object> registerApply(@RequestBody JSONObject request) {
        ApiRegister apiReg = new ApiRegister();

        apiReg.setUserId(request.getString("userId"));
        apiReg.setName(request.getString("name"));
        apiReg.setPrice(request.getDouble("price"));
        apiReg.setProtocol(InternetProtocol.valueOf(request.getString("protocol")));
        apiReg.setUrl(request.getString("url"));
        apiReg.setMethod(HttpMethod.valueOf(request.getString("method")));
        apiReg.setIntroduction(request.getString("introduction"));
        apiReg.setVisible(ApiVisible.valueOf(request.getString("visible")));
        apiReg.setParam(request.getList("param", ApiParamItem.class));
        apiReg.setQuery(request.getList("query", ApiQueryItem.class));
        apiReg.setRequestHeader(request.getList("requestHeader", ApiHeaderItem.class));
        apiReg.setRequestBody(request.getObject("requestBody", ApiBody.class));
        apiReg.setResponseHeader(request.getList("responseHeader", ApiHeaderItem.class));
        apiReg.setResponseBody(request.getObject("responseBody", ApiBody.class));
        apiReg.setApplyStatus(ApplyStatus.PENDING);

        boolean success = apiService.registerApply(apiReg);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "注册申请成功", success));
    }

    @GetMapping("/register/apply/list")
    public ResponseEntity<Object> registerApplyList() {
        User user = AuthUtil.getUser();

        List<ApiRegister> apiRegs = apiService.registerApplyList(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiRegisterVOList(apiRegs)));
    }

    @PostMapping("/register/apply/detail")
    public ResponseEntity<Object> registerApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        logger.info(request);

        ApiRegister apiReg = apiService.registerApplyDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiRegisterVO(apiReg)));
    }

    @GetMapping("/register/approval/list")
    public ResponseEntity<Object> registerApprovalList() {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        List<ApiRegister> apiRegs = apiService.registerApprovalList(user.getOrganizationId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiRegisterVOList(apiRegs)));
    }

    @PostMapping("/register/approval/detail")
    public ResponseEntity<Object> registerApprovalDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员无法查看API注册申请列表");
        }

        ApiRegister apiReg = apiService.registerApprovalDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiRegisterVO(apiReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员无法查看API注册申请列表");
        }

        boolean success = apiService.registerReply(applyId, reply, reason);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @GetMapping("/list/my")
    public ResponseEntity<Object> myApiList() {
        User user = AuthUtil.getUser();

        List<Api> apis = apiService.myApiList(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVOList(apis)));
    }

    @GetMapping("/list/all")
    public ResponseEntity<Object> allApiList() {
        User user = AuthUtil.getUser();

        List<Api> apis = apiService.allApiList(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVOList(apis)));
    }

    @PostMapping("/information/detail")
    public ResponseEntity<Object> informationDetail(@RequestBody JSONObject request) {
        String apiId = request.getString("apiId");
        String version = request.getString("version");

        User user = AuthUtil.getUser();

        Api api = apiService.informationDetail(apiId, version, user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVO(api)));
    }

    @PutMapping("/information/update")
    public ResponseEntity<Object> informationUpdate(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        Api api = new Api();

        api.setId(request.getString("apiId"));
        api.setName(request.getString("name"));
        api.setPrice(request.getDouble("price"));
        api.setProtocol(InternetProtocol.valueOf(request.getString("protocol")));
        api.setUrl(request.getString("url"));
        api.setMethod(HttpMethod.valueOf(request.getString("method")));
        api.setIntroduction(request.getString("introduction"));
        api.setVisible(ApiVisible.valueOf(request.getString("visible")));
        api.setParam(request.getList("param", ApiParamItem.class));
        api.setQuery(request.getList("query", ApiQueryItem.class));
        api.setRequestHeader(request.getList("requestHeader", ApiHeaderItem.class));
        api.setRequestBody(request.getObject("requestBody", ApiBody.class));
        api.setResponseHeader(request.getList("responseHeader", ApiHeaderItem.class));
        api.setResponseBody(request.getObject("responseBody", ApiBody.class));

        captchaService.verify(user.getEmail(), request.getString("code"));

        boolean success = apiService.informationUpdate(api);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));

    }

    @PostMapping("/information/history")
    public ResponseEntity<Object> informationHistory(@RequestBody JSONObject request) {
        String apiId = request.getString("apiId");

        List<Api> apis = apiService.informationHistory(apiId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVOList(apis)));
    }

    @PostMapping("/information/rollback")
    public ResponseEntity<Object> informationRollback(@RequestBody JSONObject request) {
        String apiId = request.getString("apiId");
        String version = request.getString("version");

        boolean success = apiService.informationRollback(apiId, version);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @GetMapping("/invoke/apply/list")
    public ResponseEntity<Object> invokeApplyList() {
        User user = AuthUtil.getUser();

        List<ApiInvokeApply> apiInvokeApplyList = apiService.invokeApplyList(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiInvokeApplyVOList(apiInvokeApplyList)));
    }

    @PostMapping("/invoke/apply")
    public ResponseEntity<Object> invokeApply(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        ApiInvokeApply apiInvokeApply = new ApiInvokeApply();
        apiInvokeApply.setApiId(request.getString("apiId"));
        apiInvokeApply.setUserId(user.getId());
        apiInvokeApply.setApplyReason(request.getString("applyReason"));
        apiInvokeApply.setRange(ApiInvokeRange.valueOf(request.getString("range")));
        apiInvokeApply.setStartTime(request.getDate("startTime"));
        apiInvokeApply.setEndTime(request.getDate("endTime"));

        boolean success = apiService.invokeApply(apiInvokeApply);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @PostMapping("/invoke/apply/detail")
    public ResponseEntity<Object> invokeApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        ApiInvokeApply apiInvokeApply = apiService.invokeApplyDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiInvokeApplyVO(apiInvokeApply)));
    }

    @GetMapping("/invoke/approval/list")
    public ResponseEntity<Object> invokeApprovalList() {
        User user = AuthUtil.getUser();

        List<ApiInvokeApply> apiInvokeApprovalList = apiService.invokeApprovalList(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiInvokeApplyVOList(apiInvokeApprovalList)));
    }

    @PostMapping("/invoke/approval/detail")
    public ResponseEntity<Object> invokeApprovalDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        ApiInvokeApply apiInvokeApply = apiService.invokeApprovalDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        ApiConvert.INSTANCE.toApiInvokeApplyVO(apiInvokeApply)));
    }

    @PostMapping("/invoke/reply")
    public ResponseEntity<Object> invokeReply(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean success = apiService.invokeReply(applyId, reply, reason);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @PostMapping("/invoke/initialize")
    public ResponseEntity<Object> invokeInitialize(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        ApiInvokeApply apiInvokeApply = apiService.invokeInitialize(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiInvokeApplyVO(apiInvokeApply)));
    }

    @PostMapping("/invoke/web")
    public ResponseEntity<Object> invokeWeb(@RequestBody JSONObject request) {
        logger.info(request);
        String applyId = request.getString("applyId");
        List<ApiParamItem> param = request.getList("param", ApiParamItem.class);
        List<ApiQueryItem> query = request.getList("query", ApiQueryItem.class);
        List<ApiHeaderItem> requestHeader = request.getList("requestHeader", ApiHeaderItem.class);
        ApiBody requestBody = request.getObject("requestBody", ApiBody.class);

        apiService.invokeWeb(applyId, param, query, requestHeader, requestBody);
        return null;
    }

    @PostMapping("/invoke/log/list")
    public ResponseEntity<Object> invokeLogList(@RequestBody JSONObject request) {
        return null;
    }

//
//    @GetMapping("/api/list/my")
//    public ResponseEntity<Object> myAPIList(HttpSession session) {
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构API失败");
//        }
//
//        LambdaQueryWrapper<API> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(API::getAuthor, login.getId());
//
//        List<API> myAPIList = apiMapper.selectList(queryWrapper);
//
//        return ResponseEntity.status(HttpStatus.OK).body(myAPIList);
//    }
//
//    @GetMapping("/api/list/all")
//    public ResponseEntity<Object> allAPIList(HttpSession session) {
//
//        LambdaQueryWrapper<API> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByAsc(API::getId).orderByAsc(API::getCreatedTime);
//
//        List<APIInfo> allAPIList = apiMapper.getAllAPIList(queryWrapper);
//
//        return ResponseEntity.status(HttpStatus.OK).body(allAPIList);
//    }
//
//
//    @PostMapping("/api/invoke/apply")
//    public ResponseEntity<Object> apiInvokeApply(@RequestBody JSONObject request, HttpSession session) {
//        logger.info(request);
//
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
//        }
//
//        API api = apiMapper.selectById(request.getString("id"));
//
//        APIInvoke apiInvoke = new APIInvoke();
//        apiInvoke.setId(Long.parseLong(request.getString("id")));
//        apiInvoke.setApplicant(Long.parseLong(request.getString("applicant")));
//        apiInvoke.setAuthor(api.getAuthor());
//        apiInvoke.setInvokeMethod(APIInvokeMethod.valueOf(request.getString("invokeMethod")));
//
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            apiInvoke.setStartTime(format.parse(request.getString("startTime")));
//            apiInvoke.setEndTime(format.parse(request.getString("endTime")));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("日期转换错误");
//        }
//        apiInvoke.setStatus(RegisterStatus.PROCESSED);
//        apiInvoke.setComment(request.getString("comment"));
//        apiInvoke.setApplyTime(new Date());
//
//        logger.info(apiInvoke);
//
//        int count = apiInvokeMapper.insert(apiInvoke);
//        if (count == 0) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("调用申请失败");
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body("调用申请成功");
//    }
//
//    @GetMapping("/api/invoke/apply/list")
//    public ResponseEntity<Object> apiInvokeApplyList(HttpSession session) {
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
//        }
//
//        LambdaQueryWrapper<APIInvoke> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(APIInvoke::getApplyTime);
//
//        List<APIInvokeApplyInfo> apiInvokeApplyList = apiInvokeMapper.getAPIInvokeApplyList(login.getId(), queryWrapper);
//
//        return ResponseEntity.status(HttpStatus.OK).body(apiInvokeApplyList);
//    }
//
//    @GetMapping("/api/invoke/approval/list")
//    public ResponseEntity<Object> apiInvokeApprovalList(HttpSession session) {
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
//        }
//
//        LambdaQueryWrapper<APIInvoke> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(APIInvoke::getApplyTime);
//
//        List<APIInvokeApprovalInfo> apiInvokeApprovalList = apiInvokeMapper.getAPIInvokeApprovalList(login.getId(), queryWrapper);
//
//        return ResponseEntity.status(HttpStatus.OK).body(apiInvokeApprovalList);
//    }
//
//    @PostMapping("/api/invoke/reply")
//    public ResponseEntity<Object> apiInvokeReply(@RequestBody JSONObject request, HttpSession session) {
//        logger.info(request);
//
//        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));
//
//        APIInvoke apiInvoke = apiInvokeMapper.selectById(Long.parseLong(request.getString("serialNumber")));
//
//        apiInvoke.setStatus(reply);
//        if (reply == RegisterStatus.REJECT) {
//            String reason = request.getString("reason");
//            apiInvoke.setReplyMessage(reason);
//        }
//        apiInvoke.setReplyTime(new Date());
//
//        apiInvokeMapper.updateById(apiInvoke);
//
//        return ResponseEntity.status(HttpStatus.OK).body(true);
//    }
//
//    @PostMapping("/api/invoke/web")
//    public ResponseEntity<Object> apiInvokeByWeb(@RequestBody JSONObject request, HttpSession session) {
//        logger.info(request);
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
//        }
//
//        // TODO: 查验有效期
//        // TODO: 查验审批状态
//        // TODO: 查验调用方式
//
//        Object response = fabricService.invokeAPI(request.getString("applicant"), request.getString("id"), "", "");
//
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
}



