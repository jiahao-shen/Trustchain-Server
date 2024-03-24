package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.dto.*;
import com.trustchain.model.enums.ApiInvokeResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("api_invoke_log")
public class ApiInvokeLog {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("log_id")
    private String logId;   // 日志ID

    @Column("apply_id")
    private String applyId; // 对应的调用申请号

    @RelationOneToOne(selfField = "applyId", targetField = "applyId")
    private ApiInvokeApply invokeApply; // 对应的调用申请对象

    @Column("result")
    private ApiInvokeResult result; // 调用结果

    @Column("invoke_user_id")
    private String invokeUserId;    // 调用的用户ID

    @RelationOneToOne(selfField = "invokeUserId", targetField = "id")
    private User invokeUser;    // 对应的调用的用户

    @Column(value = "param", typeHandler = Fastjson2TypeHandler.class)
    private List<ApiParamItem> param;   // param参数

    @Column(value = "query", typeHandler = Fastjson2TypeHandler.class)
    private List<ApiQueryItem> query;   // query参数

    @Column(value = "request_header", typeHandler = Fastjson2TypeHandler.class)
    private List<ApiHeaderItem> requestHeader;  // 请求头

    @Column(value = "request_body", typeHandler = Fastjson2TypeHandler.class)
    private ApiRequestBody requestBody;    // 请求体

    @Column(value = "response_header", typeHandler = Fastjson2TypeHandler.class)
    private List<ApiHeaderItem> responseHeader; // 返回头

    @Column(value = "response_body", typeHandler = Fastjson2TypeHandler.class)
    private ApiResponseBody responseBody;   // 返回体

    @Column(value = "time", onInsertValue = "now()")
    private Date time;  // 调用时间
}
