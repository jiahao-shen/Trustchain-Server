package com.trustchain.model.vo;

import com.alibaba.fastjson2.JSONWriter.Feature;
import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.RegisterStatus;
import com.trustchain.model.enums.UserRole;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.serializer.MinioURLSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterVO {
    private String regId;  // 注册号

    private RegisterStatus regStatus;  // 申请状态

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

    private Organization organization;  // 所属机构

    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String logo; // 用户Logo
}
