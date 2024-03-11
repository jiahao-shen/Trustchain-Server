package com.trustchain.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.trustchain.enums.UserRole;
import com.trustchain.util.MinioURLSerializer;
import com.trustchain.model.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation {
    private String id;  // 用户ID

    private String username;    // 用户名

    private String telephone;   // 电话

    private String email;   // 邮箱

    private UserRole role; // 角色

    private Organization organization;  // 所属机构

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;  // 注册时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;  // 最后修改时间

    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String logo; // 用户Logo

    private String version; // 版本号
}
