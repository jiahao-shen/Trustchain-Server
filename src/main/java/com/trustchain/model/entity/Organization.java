package com.trustchain.model.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.KeyType;
import com.trustchain.enums.OrganizationType;
import com.trustchain.minio.MinioURLSerializer;
import com.mybatisflex.core.keygen.KeyGenerators;

import lombok.Data;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alibaba.fastjson.annotation.JSONField;

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
    private String superiorID; // 上级机构ID

    @Column("creation_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;   // 创建时间

    @Column(value = "registration_time", onInsertValue = "now()")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;   // 注册时间

    @Column(value = "last_modified", onInsertValue = "now()", onUpdateValue = "now()")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;   // 最后修改时间

    @Column("version")
    private String version;  // 版本号

    @Column("logo")
    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String logo; // 机构Logo

    @Column("file")
    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String file;    // 机构文件
}
