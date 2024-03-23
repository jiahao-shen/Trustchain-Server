package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.ApiInvokeRange;
import com.trustchain.model.enums.ApiInvokeStatus;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.vo.ApiInvokeApplyVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("api_invoke_apply")
public class ApiInvokeApply {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("apply_id")
    @ColumnAlias("api_invoke_apply_apply_id")
    private String applyId; // 申请号

    @Column("apply_status")
    @ColumnAlias("api_invoke_apply_apply_status")
    private ApplyStatus applyStatus = ApplyStatus.PENDING;

    @Column(value = "apply_time", onInsertValue = "now()")
    @ColumnAlias("api_invoke_apply_apply_time")
    private Date applyTime;    // 申请时间

    @Column("apply_reason")
    @ColumnAlias("api_invoke_apply_apply_reason")
    private String applyReason; // 申请理由

    @Column("reply_time")
    @ColumnAlias("api_invoke_apply_reply_time")
    private Date replyTime;   // 回复时间

    @Column("reply_reason")
    @ColumnAlias("api_invoke_apply_reply_reason")
    private String replyReason;    // 回复理由

    @Column("invoke_status")
    @ColumnAlias("api_invoke_apply_invoke_status")
    private ApiInvokeStatus invokeStatus = ApiInvokeStatus.PENDING;   // 调用状态

    @Column("api_id")
    @ColumnAlias("api_invoke_apply_api_id")
    private String apiId;   // 申请的API的ID

    @RelationOneToOne(selfField = "apiId", targetField = "id")
    private Api api;    // 申请的API

    @Column("user_id")
    @ColumnAlias("api_invoke_apply_user_id")
    private String userId;  // 申请用户的ID

    @RelationOneToOne(selfField = "userId", targetField = "id")
    @ColumnAlias("api_invoke_apply_user")
    private User user;  // 申请用户

    @Column("range")
    @ColumnAlias("api_invoke_apply_range")
    private ApiInvokeRange range;   // 申请范围

    @Column(value = "start_time")
    @ColumnAlias("api_invoke_apply_start_time")
    private Date startTime;    // 开始时间

    @Column(value = "end_time")
    @ColumnAlias("api_invoke_apply_end_time")
    private Date endTime;    // 结束时间

    @Column(value = "app_key")
    @ColumnAlias("api_invoke_apply_app_key")
    private String appKey;

    @Column(value = "secret_key")
    @ColumnAlias("api_invoke_apply_secret_key")
    private String secretKey;

    @Column(value = "is_delete", isLogicDelete = true)
    @ColumnAlias("api_invoke_apply_is_delete")
    private Boolean isDelete;   // 逻辑删除标志位
}
