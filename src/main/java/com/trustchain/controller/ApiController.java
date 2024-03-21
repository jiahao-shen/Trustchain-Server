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
import com.trustchain.model.vo.ApiInvokeApplyVO;
import com.trustchain.model.vo.ApiRegisterVO;
import com.trustchain.model.vo.ApiVO;
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

import java.io.IOException;
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
    @ResponseBody
    public BaseResponse<Boolean> registerApply(@RequestBody JSONObject request) {
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

        return new BaseResponse(StatusCode.SUCCESS, "注册申请成功", success);
    }

    @GetMapping("/register/apply/list")
    @ResponseBody
    public BaseResponse<List<ApiRegisterVO>> registerApplyList() {
        User user = AuthUtil.getUser();

        List<ApiRegister> apiRegs = apiService.registerApplyList(user.getId());

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiRegisterVOList(apiRegs));
    }

    @PostMapping("/register/apply/detail")
    @ResponseBody
    public BaseResponse<ApiRegisterVO> registerApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        logger.info(request);

        ApiRegister apiReg = apiService.registerApplyDetail(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiRegisterVO(apiReg));
    }

    @GetMapping("/register/approval/list")
    @ResponseBody
    public BaseResponse<List<ApiRegisterVO>> registerApprovalList() {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法查看API注册审批列表");
        }

        List<ApiRegister> apiRegs = apiService.registerApprovalList(user.getOrganizationId());

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiRegisterVOList(apiRegs));
    }

    @PostMapping("/register/approval/detail")
    @ResponseBody
    public BaseResponse<ApiRegisterVO> registerApprovalDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员无法查看API注册审批详情");
        }

        ApiRegister apiReg = apiService.registerApprovalDetail(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiRegisterVO(apiReg));
    }

    @PostMapping("/register/reply")
    @ResponseBody
    public BaseResponse<Boolean> registerReply(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        if (!AuthUtil.getUser().isAdmin()) {
            throw new NoPermissionException("非管理员无法进行API注册审批");
        }

        boolean success = apiService.registerReply(applyId, reply, reason);

        return new BaseResponse(StatusCode.SUCCESS, "", success);
    }

    @GetMapping("/list/my")
    @ResponseBody
    public BaseResponse<List<ApiVO>> myApiList() {
        User user = AuthUtil.getUser();

        List<Api> apis = apiService.myApiList(user);

        return new BaseResponse(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVOList(apis));
    }

    @GetMapping("/list/all")
    @ResponseBody
    public BaseResponse<List<ApiVO>> allApiList() {
        User user = AuthUtil.getUser();

        List<Api> apis = apiService.allApiList(user);

        return new BaseResponse(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVOList(apis));
    }

    @PostMapping("/information/detail")
    @ResponseBody
    public BaseResponse<ApiVO> informationDetail(@RequestBody JSONObject request) {
        String apiId = request.getString("apiId");
        String version = request.getString("version");

        User user = AuthUtil.getUser();

        Api api = apiService.informationDetail(apiId, version, user.getId());

        return new BaseResponse(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVO(api));
    }

    @PutMapping("/information/update")
    @ResponseBody
    public BaseResponse<Boolean> informationUpdate(@RequestBody JSONObject request) {
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

        return new BaseResponse(StatusCode.SUCCESS, "", success);

    }

    @PostMapping("/information/history")
    @ResponseBody
    public BaseResponse<List<ApiVO>> informationHistory(@RequestBody JSONObject request) {
        String apiId = request.getString("apiId");

        List<Api> apis = apiService.informationHistory(apiId);

        return new BaseResponse(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiVOList(apis));
    }

    @PostMapping("/information/rollback")
    @ResponseBody
    public BaseResponse<Boolean> informationRollback(@RequestBody JSONObject request) {
        String apiId = request.getString("apiId");
        String version = request.getString("version");

        boolean success = apiService.informationRollback(apiId, version);

        return new BaseResponse(StatusCode.SUCCESS, "", success);
    }

    @GetMapping("/invoke/apply/list")
    @ResponseBody
    public BaseResponse<List<ApiInvokeApplyVO>> invokeApplyList() {
        User user = AuthUtil.getUser();

        List<ApiInvokeApply> apiInvokeApplyList = apiService.invokeApplyList(user.getId());

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiInvokeApplyVOList(apiInvokeApplyList));
    }

    @PostMapping("/invoke/apply")
    @ResponseBody
    public BaseResponse<Boolean> invokeApply(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        ApiInvokeApply apiInvokeApply = new ApiInvokeApply();
        apiInvokeApply.setApiId(request.getString("apiId"));
        apiInvokeApply.setUserId(user.getId());
        apiInvokeApply.setApplyReason(request.getString("applyReason"));
        apiInvokeApply.setRange(ApiInvokeRange.valueOf(request.getString("range")));
        apiInvokeApply.setStartTime(request.getDate("startTime"));
        apiInvokeApply.setEndTime(request.getDate("endTime"));

        boolean success = apiService.invokeApply(apiInvokeApply);

        return new BaseResponse(StatusCode.SUCCESS, "", success);
    }

    @PostMapping("/invoke/apply/detail")
    @ResponseBody
    public BaseResponse<List<ApiInvokeApplyVO>> invokeApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        ApiInvokeApply apiInvokeApply = apiService.invokeApplyDetail(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiInvokeApplyVO(apiInvokeApply));
    }

    @GetMapping("/invoke/approval/list")
    @ResponseBody
    public BaseResponse<List<ApiInvokeApplyVO>> invokeApprovalList() {
        User user = AuthUtil.getUser();

        List<ApiInvokeApply> apiInvokeApprovalList = apiService.invokeApprovalList(user.getId());

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiInvokeApplyVOList(apiInvokeApprovalList));
    }

    @PostMapping("/invoke/approval/detail")
    @ResponseBody
    public BaseResponse<ApiInvokeApplyVO> invokeApprovalDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        ApiInvokeApply apiInvokeApply = apiService.invokeApprovalDetail(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                ApiConvert.INSTANCE.toApiInvokeApplyVO(apiInvokeApply));
    }

    @PostMapping("/invoke/reply")
    @ResponseBody
    public BaseResponse<Boolean> invokeReply(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean success = apiService.invokeReply(applyId, reply, reason);

        return new BaseResponse(StatusCode.SUCCESS, "", success);
    }

    @PostMapping("/invoke/initialize")
    @ResponseBody
    public BaseResponse<ApiInvokeApplyVO> invokeInitialize(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        ApiInvokeApply apiInvokeApply = apiService.invokeInitialize(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "", ApiConvert.INSTANCE.toApiInvokeApplyVO(apiInvokeApply));
    }

    @PostMapping("/invoke/web")
    @ResponseBody
    public BaseResponse<?> invokeWeb(@RequestBody JSONObject request) throws IOException {
        logger.info(request);

        String applyId = request.getString("applyId");
        List<ApiParamItem> param = request.getList("param", ApiParamItem.class);
        List<ApiQueryItem> query = request.getList("query", ApiQueryItem.class);
        List<ApiHeaderItem> requestHeader = request.getList("requestHeader", ApiHeaderItem.class);
        ApiBody requestBody = request.getObject("requestBody", ApiBody.class);

        apiService.invokeWeb(applyId, param, query, requestHeader, requestBody);

        return new BaseResponse(StatusCode.SUCCESS, "", null);
    }

    @PostMapping("/invoke/log/list")
    @ResponseBody
    public BaseResponse<?> invokeLogList(@RequestBody JSONObject request) {
        return new BaseResponse<>(StatusCode.SUCCESS, "", null);
    }

}



