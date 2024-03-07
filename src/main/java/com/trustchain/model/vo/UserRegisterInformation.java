package com.trustchain.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.enums.UserRole;
import com.trustchain.minio.MinioURLSerializer;
import com.trustchain.model.entity.Organization;

import java.util.Date;

public class UserRegisterInformation {
    private String regID;  // 注册号

    private RegisterStatus regStatus;  // 申请状态

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;    // 申请时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss", serialzeFeatures = SerializerFeature.WriteMapNullValue)
    private Date replyTime;   // 批复时间

    @JSONField(serialzeFeatures = SerializerFeature.WriteNullStringAsEmpty)
    private String replyMessage;    // 批复内容

    private String id;  // 用户ID

    private String username;    // 用户名

    private String telephone;   // 电话

    private String email;   // 邮箱

    private UserRole role; // 角色

    private Organization organization;  // 所属机构

    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String logo; // 用户Logo
}
