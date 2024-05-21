package com.hawkeye.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.UserRole;
import com.hawkeye.model.serializer.MinioUrlSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private String id;  // 用户ID

    private String username;    // 用户名

    private String telephone;   // 电话

    private String email;   // 邮箱

    private UserRole role; // 角色

    private OrganizationVO organization;  // 所属机构

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;  // 注册时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;  // 最后修改时间

    @JSONField(serializeUsing = MinioUrlSerializer.class)
    private String logo; // 用户Logo

    private String version; // 版本号

    private Boolean latest; // 是否为最新
}
