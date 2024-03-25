package com.trustchain.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.ApiInvokeRange;
import com.trustchain.model.enums.ApiInvokeStatus;
import com.trustchain.model.enums.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiInvokeApplyVO {
    private String applyId; // 申请号

    private ApplyStatus applyStatus = ApplyStatus.PENDING;  // 申请状态

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;    // 申请时间

    private String applyReason; // 申请理由

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;   // 回复时间

    private String replyReason;    // 回复理由

    private ApiInvokeStatus invokeStatus = ApiInvokeStatus.PENDING;   // 调用状态

    private ApiVO api;    // 申请的API

    private UserVO user;  // 申请用户

    private ApiInvokeRange range;   // 申请范围

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;    // 开始时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;    // 结束时间

    private String appKey;

    private String secretKey;
}
