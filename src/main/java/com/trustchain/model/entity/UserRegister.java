package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.enums.UserRole;
import com.trustchain.minio.MinioURLSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alibaba.fastjson.annotation.JSONField;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("user_register")
public class UserRegister {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("reg_id")
    private String regID;  // 注册号

    @Column("reg_status")
    private RegisterStatus regStatus;  // 申请状态

    @Column(value = "apply_time", onInsertValue = "now()")
    private Date applyTime;    // 申请时间

    @Column("reply_time")
    private Date replyTime;   // 批复时间

    @Column("reply_message")
    private String replyMessage;    // 批复内容

    @Column("id")
    private String id;  // 用户ID

    @Column("username")
    private String username;    // 用户名

    @Column("password")
    private String password;    // 密码

    @Column("telephone")
    private String telephone;   // 电话

    @Column("email")
    private String email;   // 邮箱

    @Column("role")
    private UserRole role; // 角色

    @Column("organization_id")
    private String organizationID;  // 所属机构ID

    @RelationOneToOne(selfField = "organizationID", targetField = "id")
    private Organization organization;  // 所属机构

    @Column("logo")
    private String logo; // 用户Logo
}

