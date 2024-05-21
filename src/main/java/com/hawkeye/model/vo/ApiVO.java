package com.hawkeye.model.vo;

import com.hawkeye.model.enums.ApiVisible;
import com.hawkeye.model.enums.HttpMethod;
import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.InternetProtocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiVO {
    private String id;  // API标识符

    private UserVO user;  // API所有者

    private String name;    // API名称

    private Double price;   //  API价格

    private InternetProtocol protocol;    // API协议

    private String url; // API地址

    private HttpMethod method;  // 请求方式

    private String introduction;    // API介绍

    private ApiVisible visible; // API可见性

    private List<ApiParamItemVO> param;   // param参数

    private List<ApiQueryItemVO> query;   // Query参数

    private List<ApiHeaderItemVO> requestHeader;   // 请求头

    private ApiRequestBodyVO requestBody; // 请求体

    private List<ApiHeaderItemVO> responseHeader;  // 返回头

    private ApiRequestBodyVO responseBody;    // 返回体

    private String version; // 版本号

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;   // 注册时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;   // 最后修改时间

    private Boolean latest; // 是否为最新
}
