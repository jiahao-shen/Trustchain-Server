package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.enums.UserRole;
import lombok.Data;


import java.util.Date;

@Data
@Table("user_register")
public class UserRegister {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("apply_id")
    @ColumnAlias("user_register_apply_id")
    private String applyId;  // 注册号

    @Column("apply_status")
    @ColumnAlias("user_register_apply_status")
    private ApplyStatus applyStatus = ApplyStatus.PENDING;  // 申请状态

    @Column(value = "apply_time", onInsertValue = "now()")
    @ColumnAlias("user_register_apply_time")
    private Date applyTime;    // 申请时间

    @Column("reply_time")
    @ColumnAlias("user_register_reply_time")
    private Date replyTime;   // 批复时间

    @Column("reply_reason")
    @ColumnAlias("user_register_reply_reason")
    private String replyReason;    // 批复内容

    @Column("id")
    @ColumnAlias("user_register_id")
    private String id;  // 用户ID

    @Column("username")
    @ColumnAlias("user_register_username")
    private String username;    // 用户名

    @Column("password")
    @ColumnAlias("user_register_password")
    private String password;    // 密码

    @Column("telephone")
    @ColumnAlias("user_register_telephone")
    private String telephone;   // 电话

    @Column("email")
    @ColumnAlias("user_register_email")
    private String email;   // 邮箱

    @Column("role")
    @ColumnAlias("user_register_role")
    private UserRole role; // 角色

    @Column("organization_id")
    @ColumnAlias("user_register_organization_id")
    private String organizationId;  // 所属机构ID

    @RelationOneToOne(selfField = "organizationId", targetField = "id")
    private Organization organization;  // 所属机构

    @Column("logo")
    @ColumnAlias("user_register_logo")
    private String logo; // 用户Logo

    @Column(value = "is_delete", isLogicDelete = true)
    @ColumnAlias("user_register_is_delete")
    private Boolean isDelete;   // 逻辑删除标志位
}

