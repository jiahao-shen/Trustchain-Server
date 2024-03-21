package com.trustchain.service;

import com.trustchain.model.dto.ApiBody;
import com.trustchain.model.dto.ApiHeaderItem;
import com.trustchain.model.dto.ApiParamItem;
import com.trustchain.model.dto.ApiQueryItem;
import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiInvokeApply;
import com.trustchain.model.entity.ApiRegister;
import com.trustchain.model.entity.User;
import com.trustchain.model.enums.ApplyStatus;

import java.io.IOException;
import java.util.List;

public interface ApiService {
    /**
     * @param api API对象
     * @return 是否注册成功
     */
    boolean register(Api api);

    /**
     * @param apiReg API注册对象
     * @return 是否成功
     */
    boolean registerApply(ApiRegister apiReg);

    /**
     * @param userId 用户ID
     * @return API注册申请列表
     */
    List<ApiRegister> registerApplyList(String userId);

    /**
     * @param applyId 注册ID
     * @return
     */
    ApiRegister registerApplyDetail(String applyId);

    /**
     * @param orgId 机构ID
     * @return API注册审批列表
     */
    List<ApiRegister> registerApprovalList(String orgId);

    /**
     * @param applyId 注册ID
     * @return API注册对象
     */
    ApiRegister registerApprovalDetail(String applyId);

    /**
     * @param applyId 注册ID
     * @param reply   回复类型
     * @param reason  回复理由
     * @return 是否回复成功
     */
    boolean registerReply(String applyId, ApplyStatus reply, String reason);

    /**
     * @param user 用户
     * @return 该用户的所有API列表
     */
    List<Api> myApiList(User user);

    /**
     * @param user 用户
     * @return 所有API列表
     */
    List<Api> allApiList(User user);

    /**
     * @param apiId   API的ID
     * @param version 版本号
     * @param userId  用户ID
     * @return API信息
     */
    Api informationDetail(String apiId, String version, String userId);

    /**
     * @param api API对象
     * @return 是否更新成功
     */
    boolean informationUpdate(Api api);

    List<Api> informationHistory(String apiId);

    /**
     * @param apiId   API的ID
     * @param version 版本号
     * @return 是否回滚成功
     */
    boolean informationRollback(String apiId, String version);

    /**
     * @param apiInvokeApply API调用申请
     * @return 是否成功
     */
    boolean invokeApply(ApiInvokeApply apiInvokeApply);

    /**
     * @param userId 用户ID
     * @return API调用申请列表
     */
    List<ApiInvokeApply> invokeApplyList(String userId);

    /**
     * @param applyId 申请号
     * @return API调用申请
     */
    ApiInvokeApply invokeApplyDetail(String applyId);

    /**
     * @param userId 用户ID
     * @return API调用审核列表
     */
    List<ApiInvokeApply> invokeApprovalList(String userId);

    /**
     * @param applyId 调用申请号
     * @return API调用申请
     */
    ApiInvokeApply invokeApprovalDetail(String applyId);

    /**
     * @param applyId 调用申请号
     * @param reply   回复类型
     * @param reason  回复理由
     * @return 是否成功
     */
    boolean invokeReply(String applyId, ApplyStatus reply, String reason);

    /**
     * @param applyId 调用申请号
     * @return API信息
     */
    ApiInvokeApply invokeInitialize(String applyId);

    /**
     * @param applyId 调用申请号
     * @param param   请求路径参数
     * @param query   请求参数
     * @param header  请求标头
     * @param body    请求体
     */
    void invokeWeb(String applyId, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiBody body) throws IOException;
}
