package com.trustchain.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.ApiVisible;
import com.trustchain.model.enums.HttpMethod;
import com.trustchain.model.enums.InternetProtocol;
import com.trustchain.model.enums.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRegisterVO {
    private String applyId;   // 注册ID

    private ApplyStatus applyStatus;  // 申请状态

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;    // 申请时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;   // 批复时间

    private String replyReason;    // 批复内容

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

    private ApiResponseBodyVO responseBody;    // 返回体
}
