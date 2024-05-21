package com.hawkeye.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.hawkeye.model.enums.ApiVisible;
import com.hawkeye.model.enums.HttpMethod;
import com.hawkeye.model.enums.InternetProtocol;
import com.hawkeye.model.enums.ApplyStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Table("api_register")
public class ApiRegister {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("apply_id")
    @ColumnAlias("api_register_apply_id")
    private String applyId;   // 注册ID

    @Column("apply_status")
    @ColumnAlias("api_register_apply_status")
    private ApplyStatus applyStatus;  // 申请状态

    @Column(value = "apply_time", onInsertValue = "now()")
    @ColumnAlias("api_register_apply_time")
    private Date applyTime;    // 申请时间

    @Column("reply_time")
    @ColumnAlias("api_register_reply_time")
    private Date replyTime;   // 批复时间

    @Column("reply_reason")
    @ColumnAlias("api_register_reply_reason")
    private String replyReason;    // 批复内容

    @Column("id")
    @ColumnAlias("api_register_id")
    private String id;  // API标识符

    @Column("user_id")
    @ColumnAlias("api_register_user_id")
    private String userId;  // API所有者ID

    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;  // API所有者

    @Column("name")
    @ColumnAlias("api_register_name")
    private String name;    // API名称

    @Column("price")
    @ColumnAlias("api_register_price")
    private Double price;   //  API价格

    @Column("protocol")
    @ColumnAlias("api_register_protocol")
    private InternetProtocol protocol;    // API协议

    @Column("url")
    @ColumnAlias("api_register_url")
    private String url; // API地址

    @Column("method")
    @ColumnAlias("api_register_method")
    private HttpMethod method;  // 请求方式

    @Column("introduction")
    @ColumnAlias("api_register_introduction")
    private String introduction;    // API介绍

    @Column("visible")
    @ColumnAlias("api_register_visible")
    private ApiVisible visible; // API可见性

    @Column(value = "param", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_register_param")
    private List<ApiParamItem> param;   // param参数

    @Column(value = "query", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_register_query")
    private List<ApiQueryItem> query;   // Query参数

    @Column(value = "request_header", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_register_request_header")
    private List<ApiHeaderItem> requestHeader;   // 请求头

    @Column(value = "request_body", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_register_request_body")
    private ApiRequestBody requestBody; // 请求体

    @Column(value = "response_header", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_register_response_header")
    private List<ApiHeaderItem> responseHeader;  // 返回头

    @Column(value = "response_body", typeHandler = Fastjson2TypeHandler.class)
    @ColumnAlias("api_register_response_body")
    private ApiResponseBody responseBody;    // 返回体

    @Column(value = "is_delete", isLogicDelete = true)
    @ColumnAlias("api_register_is_delete")
    private Boolean isDelete;   // 逻辑删除标志位
}
