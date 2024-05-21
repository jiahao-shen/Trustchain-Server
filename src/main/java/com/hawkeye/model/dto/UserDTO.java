package com.hawkeye.model.dto;

import com.hawkeye.model.entity.Wallet;
import com.hawkeye.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;  // 用户ID

    private String username;    // 用户名

    private String telephone;   // 电话

    private String email;   // 邮箱

    private UserRole role; // 角色

    private String organizationId;  // 机构ID

    private OrganizationDTO organization;  // 所属机构

    private Date registrationTime;  // 注册时间

    private Date lastModified;  // 最后修改时间

    private String logo; // 用户Logo

    private String version; // 版本号

    private String walletId;    // 钱包ID

    private Wallet wallet;  // 钱包

    private Boolean latest; // 是否为最新
}
