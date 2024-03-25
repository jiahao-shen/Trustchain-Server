package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.ApiInvokeMethod;
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
    @ColumnAlias("api_invoke_log_log_id")
    private String logId;   // 日志ID

    @Column("apply_id")
    @ColumnAlias("api_invoke_log_apply_id")
    private String applyId; // 对应的调用申请号

    @RelationOneToOne(selfField = "applyId", targetField = "applyId")
    private ApiInvokeApply invokeApply; // 对应的调用申请对象

    @Column("invoke_user_id")
    @ColumnAlias("api_invoke_log_inovke_user_id")
    private String invokeUserId;    // 调用的用户ID

    @Column("invoke_method")
    @ColumnAlias("api_invoke_log_inovke_method")
    private ApiInvokeMethod invokeMethod;   // 调用方式

    @RelationOneToOne(selfField = "invokeUserId", targetField = "id")
    private User invokeUser;    // 对应的调用的用户

    @Column(value = "param", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_invoke_log_param")
    private List<ApiParamItem> param;   // param参数

    @Column(value = "query", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_invoke_log_query")
    private List<ApiQueryItem> query;   // query参数

    @Column(value = "request_header", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_invoke_log_request_header")
    private List<ApiHeaderItem> requestHeader;  // 请求标头

    @Column(value = "request_body", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_invoke_log_request_body")
    private ApiRequestBody requestBody;    // 请求体

    @Column("status_code")
    @ColumnAlias("api_invoke_log_status_code")
    private Integer statusCode; // 调用结果

    @Column(value = "response_header", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_invoke_log_response_header")
    private List<ApiHeaderItem> responseHeader; // 返回标头

    @Column(value = "response_body", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_invoke_log_response_body")
    private ApiResponseBody responseBody;   // 返回体

    @Column("error_message")
    @ColumnAlias("api_invoke_log_error_message")
    private String errorMessage;    // 错误信息

    @Column(value = "time", onInsertValue = "now()")
    @ColumnAlias("api_invoke_log_time")
    private Date time;  // 调用时间

}
