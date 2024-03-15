package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.APIVisible;
import com.trustchain.model.enums.HttpMethod;
import com.trustchain.model.enums.InternetProtocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("api")
public class API {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("id")
    private String id;  // API标识符

    @Column("user_id")
    private String userId;  // API所有者ID

    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;  // API所有者

    @Column("name")
    private String name;    // API名称

    @Column("price")
    private Double price;   //  API价格

    @Column("protocol")
    private InternetProtocol protocol;    // API协议

    @Column("url")
    private String url; // API地址

    @Column("method")
    private HttpMethod method;  // 请求方式

    @Column("introduction")
    private String introduction;    // API介绍

    @Column("visible")
    private APIVisible visible; // API可见性

    @Column(value = "param", typeHandler = Fastjson2TypeHandler.class)
    private List<APIParamItem> param;   // param参数

    @Column(value = "query", typeHandler = Fastjson2TypeHandler.class)
    private List<APIQueryItem> query;   // Query参数

    @Column(value = "request_header", typeHandler = Fastjson2TypeHandler.class)
    private List<APIHeaderItem> requestHeader;   // 请求头

    @Column(value = "request_body", typeHandler = Fastjson2TypeHandler.class)
    private APIBody requestBody; // 请求体

    @Column(value = "response_header", typeHandler = Fastjson2TypeHandler.class)
    private List<APIHeaderItem> responseHeader;  // 返回头

    @Column(value = "response_body", typeHandler = Fastjson2TypeHandler.class)
    private APIBody responseBody;    // 返回体

    @Column("version")
    private String version; // 版本号

    @Column(value = "registration_time", onInsertValue = "now()")
    private Date registrationTime;   // 注册时间

    @Column(value = "last_modified", onInsertValue = "now()", onUpdateValue = "now()")
    private Date lastModified;   // 最后修改时间

    @Column(value = "is_delete", isLogicDelete = true)
    private Boolean isDelete;   // 逻辑删除标志位
}
