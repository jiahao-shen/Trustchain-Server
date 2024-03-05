package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("user")
public class User {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
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
    private String organizationID;  // 机构ID

    @RelationOneToOne(selfField = "organizationID", targetField = "id")
    private Organization organization;

    @Column(value = "registration_time", onInsertValue = "now()")
    private Date registrationTime;  // 注册时间

    @Column(value = "last_modified", onInsertValue = "now()", onUpdateValue = "now()")
    private Date lastModified;  // 最后修改时间

    @Column("logo")
    private String logo; // 用户Logo

    @Column("version")
    private String version; // 版本号
}

