package com.trustchain.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.ApiInvokeResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(value = "time", onInsertValue = "now()")
    private Date time;  // 调用时间
}
