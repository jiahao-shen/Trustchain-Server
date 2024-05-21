package com.hawkeye.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.ApplyStatus;
import com.hawkeye.model.enums.UserRole;
import com.hawkeye.model.serializer.MinioUrlSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterVO {
    private String applyId;  // 注册号

    private ApplyStatus applyStatus;  // 申请状态

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;    // 申请时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;   // 批复时间

    private String replyReason;    // 批复内容

    private String id;  // 用户ID

    private String username;    // 用户名

    private String telephone;   // 电话

    private String email;   // 邮箱

    private UserRole role; // 角色

    private OrganizationVO organization;  // 所属机构

    @JSONField(serializeUsing = MinioUrlSerializer.class)
    private String logo; // 用户Logo
}
