package com.trustchain.model.vo;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.ApiInvokeMethod;
import com.trustchain.util.AuthUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiInvokeLogVO {
    private String id;   // 日志ID

    private String applyId; // 对应的调用申请号

    private ApiInvokeApplyVO invokeApply; // 对应的调用申请对象

    private String invokeUserId;    // 调用的用户ID

    private UserVO invokeUser;    // 对应的调用的用户

    private ApiInvokeMethod invokeMethod;   // 调用方式

    private List<ApiParamItemVO> param;   // param参数

    private List<ApiQueryItemVO> query;   // query参数

    private List<ApiHeaderItemVO> requestHeader;  // 请求标头

    private ApiRequestBodyVO requestBody;    // 请求体

    private Integer statusCode; // 调用结果

    private List<ApiHeaderItemVO> responseHeader; // 返回标头

    private ApiResponseBodyVO responseBody;   // 返回体

    private String errorMessage;    // 错误信息

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;  // 调用时间
//
//    public Boolean isFrom() {
//        return invokeUserId.equals(AuthUtil.getUser().getId());
//    }
}
