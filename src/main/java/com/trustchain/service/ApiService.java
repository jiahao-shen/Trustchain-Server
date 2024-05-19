package com.trustchain.service;

import com.mybatisflex.core.paginate.Page;
import com.trustchain.model.dto.ApiDTO;
import com.trustchain.model.entity.ApiRequestBody;
import com.trustchain.model.entity.ApiHeaderItem;
import com.trustchain.model.entity.ApiParamItem;
import com.trustchain.model.entity.ApiQueryItem;
import com.trustchain.model.entity.*;
import com.trustchain.model.enums.ApplyStatus;
import okhttp3.Response;
import org.chainmaker.pb.common.ResultOuterClass;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * @param userId     用户ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return API注册申请列表
     */
    Page<ApiRegister> registerApplyList(String userId,
                                        Integer pageNumber,
                                        Integer pageSize,
                                        Map<String, List<String>> filter,
                                        Map<String, String> sort);

    /**
     * @param applyId 注册ID
     * @return API注册申请详情
     */
    ApiRegister registerApplyDetail(String applyId);

    /**
     * @param orgId 机构ID
     * @return API注册审批列表
     */
    List<ApiRegister> registerApprovalList(String orgId);

    /**
     * @param orgId      机构ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return API注册审批列表
     */
    Page<ApiRegister> registerApprovalList(String orgId,
                                           Integer pageNumber,
                                           Integer pageSize,
                                           Map<String, List<String>> filter,
                                           Map<String, String> sort);

    /**
     * @param applyId 注册ID
     * @return API注册对象
     */
    ApiRegister registerApprovalDetail(String applyId);

    /**
     * @param applyId 注册ID
     * @param reply   回复类型
     * @param reason  回复理由
     */
    void registerReply(String applyId, ApplyStatus reply, String reason);

    /**
     * @param user 用户
     * @return 该用户的所有API列表
     */
    List<Api> myApiList(User user);

    /**
     * @param user       用户
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return 我的API列表
     */
    Page<Api> myApiList(User user,
                        Integer pageNumber,
                        Integer pageSize,
                        Map<String, List<String>> filter,
                        Map<String, String> sort);

    /**
     * @param user 用户
     * @return 我的API列表
     */
    List<Api> allApiList(User user);

    /**
     * @param user 用户
     * @return 所有API列表
     */
    Page<Api> allApiList(User user,
                         String search,
                         Integer pageNumber,
                         Integer pageSize,
                         Map<String, List<String>> filter,
                         Map<String, String> sort);

    /**
     * @param apiId   API的ID
     * @param version 版本号
     * @param userId  用户ID
     * @return API信息
     */
    ApiDTO informationDetail(String apiId, String version, String userId);

    /**
     * @param api API对象
     * @return 是否更新成功
     */
    boolean informationUpdate(Api api);

    /**
     * @param apiId API的ID
     * @return API信息的历史记录
     */
    List<ApiDTO> informationHistory(String apiId);

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
     * @param userId     用户ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return API调用申请列表
     */
    Page<ApiInvokeApply> invokeApplyList(String userId,
                                         Integer pageNumber,
                                         Integer pageSize,
                                         Map<String, List<String>> filter,
                                         Map<String, String> sort);

    /**
     * @param applyId 申请号
     * @return API调用申请详情
     */
    ApiInvokeApply invokeApplyDetail(String applyId);

    /**
     * @param userId 用户ID
     * @return API调用审批列表
     */
    List<ApiInvokeApply> invokeApprovalList(String userId);

    /**
     * @param userId     用户ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return API调用审批列表
     */
    Page<ApiInvokeApply> invokeApprovalList(String userId,
                                            Integer pageNumber,
                                            Integer pageSize,
                                            Map<String, List<String>> filter,
                                            Map<String, String> sort);

    /**
     * @param applyId 调用申请号
     * @return API调用申请
     */
    ApiInvokeApply invokeApprovalDetail(String applyId);

    /**
     * @param applyId 调用申请号
     * @param reply   回复类型
     * @param reason  回复理由
     */
    void invokeReply(String applyId, ApplyStatus reply, String reason);

    /**
     * @param applyId 调用申请号
     * @return API信息
     */
    ApiInvokeApply invokeInitialize(String applyId);

    /**
     * @param applyId      调用申请号
     * @param invokeUserId 调用人ID
     * @param param        请求路径参数
     * @param query        请求参数
     * @param header       请求标头
     * @param body         请求体
     */
    void invokeWeb(String applyId, String invokeUserId, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiRequestBody body) throws IOException;

    /**
     * @param appKey    调用申请号
     * @param secretKey 调用人ID
     * @param param     请求路径参数
     * @param query     请求参数
     * @param header    请求标头
     * @param body      请求体
     */
    ApiInvokeLog invokeSDK(String appKey, String secretKey, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiRequestBody body);

    /**
     * @param userId  用户ID
     * @param applyId 调用申请号
     * @return 调用日志列表
     */
    List<ApiInvokeLog> invokeLogList(String userId,
                                     String applyId);

    /**
     * @param userId     用户ID
     * @param applyId    调用申请号
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤器
     * @param sort       排序
     * @return 调用日志列表
     */
    Page<ApiInvokeLog> invokeLogList(String userId,
                                     String applyId,
                                     String search,
                                     Integer pageNumber,
                                     Integer pageSize,
                                     Map<String, List<String>> filter,
                                     Map<String, String> sort);

    /**
     * @param id 日志ID
     * @return Api调用日志
     */
    ApiInvokeLog invokeLogDetail(String id);
}
