package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.trustchain.model.enums.OrganizationType;
import com.mybatisflex.core.keygen.KeyGenerators;

import lombok.Data;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("organization")
public class Organization {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
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
    private String superiorId; // 上级机构ID

    @RelationOneToOne(selfField = "superiorId", targetField = "id")
    private Organization superior;  // 上级机构

    @Column("creation_time")
    private Date creationTime;   // 创建时间

    @Column(value = "registration_time", onInsertValue = "now()")
    private Date registrationTime;   // 注册时间

    @Column(value = "last_modified", onInsertValue = "now()", onUpdateValue = "now()")
    private Date lastModified;   // 最后修改时间

    @Column("version")
    private String version;  // 版本号

    @Column("logo")
    private String logo; // 机构Logo

    @Column("file")
    private String file;    // 机构文件

    @Column(value = "is_delete", isLogicDelete = true)
    private Boolean isDelete;   // 逻辑删除标志位

}
