package com.hawkeye.model.dto;

import com.hawkeye.model.entity.*;
import com.hawkeye.model.enums.ApiVisible;
import com.hawkeye.model.enums.HttpMethod;
import com.hawkeye.model.enums.InternetProtocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiDTO {
    private String id;  // API标识符

    private String userId;  // API所有者ID

    private UserDTO user;  // API所有者

    private String name;    // API名称

    private Double price;   //  API价格

    private InternetProtocol protocol;    // API协议

    private String url; // API地址

    private HttpMethod method;  // 请求方式

    private String introduction;    // API介绍

    private ApiVisible visible; // API可见性

    private List<ApiParamItem> param;   // param参数

    private List<ApiQueryItem> query;   // Query参数

    private List<ApiHeaderItem> requestHeader;   // 请求头

    private ApiRequestBody requestBody; // 请求体

    private List<ApiHeaderItem> responseHeader;  // 返回头

    private ApiResponseBody responseBody;    // 返回体

    private String version; // 版本号

    private Date registrationTime;   // 注册时间

    private Date lastModified;   // 最后修改时间

    private Boolean latest;   // 逻辑删除标志位
}
