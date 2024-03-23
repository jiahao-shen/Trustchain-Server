package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.OrganizationType;
import com.trustchain.model.enums.ApplyStatus;
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
    @Column("apply_id")
    @ColumnAlias("organization_register_apply_id")
    private String applyId;  // 注册ID

    @Column("apply_status")
    @ColumnAlias("organization_register_apply_status")
    private ApplyStatus applyStatus = ApplyStatus.PENDING;  // 申请状态

    @Column(value = "apply_time", onInsertValue = "now()")
    @ColumnAlias("organization_register_apply_time")
    private Date applyTime;    // 申请时间

    @Column("reply_time")
    @ColumnAlias("organization_register_reply_time")
    private Date replyTime;   // 批复时间

    @Column("reply_reason")
    @ColumnAlias("organization_register_reply_reason")
    private String replyReason;    // 批复内容

    @Column("id")
    @ColumnAlias("organization_register_id")
    private String id;  // 机构ID

    @Column("name")
    @ColumnAlias("organization_register_name")
    private String name;    // 机构名称

    @Column("type")
    @ColumnAlias("organization_register_type")
    private OrganizationType type;    // 机构类型

    @Column("telephone")
    @ColumnAlias("organization_register_telephone")
    private String telephone;   // 机构电话

    @Column("email")
    @ColumnAlias("organization_register_email")
    private String email;   // 机构邮箱

    @Column("city")
    @ColumnAlias("organization_register_city")
    private String city;    // 机构城市

    @Column("address")
    @ColumnAlias("organization_register_address")
    private String address; // 机构地址

    @Column("introduction")
    @ColumnAlias("organization_register_introduction")
    private String introduction;    // 机构介绍

    @Column("superior_id")
    @ColumnAlias("organization_register_superior_id")
    private String superiorId; // 上级机构ID

    @RelationOneToOne(selfField = "superiorId", targetField = "id")
    private Organization superior;  // 上级机构

    @Column("creation_time")
    @ColumnAlias("organization_register_creation_time")
    private Date creationTime;   // 创建时间

    @Column("logo")
    @ColumnAlias("organization_register_logo")
    private String logo; // 机构Logo

    @Column("file")
    @ColumnAlias("organization_register_file")
    private String file;    // 机构文件

    @Column(value = "is_delete", isLogicDelete = true)
    @ColumnAlias("organization_is_delete")
    private Boolean isDelete;   // 逻辑删除标志位
}
