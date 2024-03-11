package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.enums.OrganizationType;
import com.trustchain.enums.RegisterStatus;
import lombok.Data;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("organization_register")
public class OrganizationRegister {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("reg_id")
    private String regID;  // 注册ID

    @Column("reg_status")
    private RegisterStatus regStatus;  // 申请状态

    @Column(value = "apply_time", onInsertValue = "now()")
    private Date applyTime;    // 申请时间

    @Column("reply_time")
    private Date replyTime;   // 批复时间

    @Column("reply_message")
    private String replyMessage;    // 批复内容

    @Column("id")
    private String id;  // 机构ID

    @Column("name")
    private String name;    // 机构名称

    @Column("type")
    private OrganizationType type;    // 机构类型

    @Column("telephone")
    private String telephone;   // 机构电话

    @Column("email")
    private String email;   // 机构邮箱

    @Column("city")
    private String city;    // 机构城市

    @Column("address")
    private String address; // 机构地址

    @Column("introduction")
    private String introduction;    // 机构介绍

    @Column("superior_id")
    private String superiorID; // 上级机构ID

    @RelationOneToOne(selfField = "superiorID", targetField = "id")
    private Organization superior;  // 上级机构

    @Column("creation_time")
    private Date creationTime;   // 创建时间

    @Column("logo")
    private String logo; // 机构Logo

    @Column("file")
    private String file;    // 机构文件
}
