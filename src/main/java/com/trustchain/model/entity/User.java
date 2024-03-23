package com.trustchain.model.entity;

import lombok.Data;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user")
public class User {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("id")
    @ColumnAlias("user_id")
    private String id;  // 用户ID

    @Column("username")
    @ColumnAlias("user_username")
    private String username;    // 用户名

    @Column("password")
    @ColumnAlias("user_password")
    private String password;    // 密码

    @Column("telephone")
    @ColumnAlias("user_telephone")
    private String telephone;   // 电话

    @Column("email")
    @ColumnAlias("user_email")
    private String email;   // 邮箱

    @Column("role")
    @ColumnAlias("user_role")
    private UserRole role; // 角色

    @Column("organization_id")
    @ColumnAlias("user_organization_id")
    private String organizationId;  // 机构ID

    @RelationOneToOne(selfField = "organizationId", targetField = "id")
    private Organization organization;

    @Column(value = "registration_time", onInsertValue = "now()")
    @ColumnAlias("user_registration_time")
    private Date registrationTime;  // 注册时间

    @Column(value = "last_modified", onInsertValue = "now()", onUpdateValue = "now()")
    @ColumnAlias("user_last_modified")
    private Date lastModified;  // 最后修改时间

    @Column("logo")
    @ColumnAlias("user_logo")
    private String logo; // 用户Logo

    @Column("version")
    @ColumnAlias("user_version")
    private String version; // 版本号

    @Column(value = "is_delete", isLogicDelete = true)
    @ColumnAlias("user_is_delete")
    private Boolean isDelete;   // 逻辑删除标志位

    public Boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}


