package com.trustchain.model;

import com.trustchain.enums.OrganizationType;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.minio.MinioURLSerializer;
import lombok.Data;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.*;
import com.alibaba.fastjson.annotation.JSONField;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("organization_register")
public class OrganizationRegister {
    @TableId(value = "serial_number", type = IdType.ASSIGN_UUID)
    private String serialNumber;  // 注册流水号

    @TableField("status")
    private RegisterStatus status;  // 申请状态

    @TableField(value = "apply_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;    // 申请时间

    @TableField(value = "reply_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;   // 批复时间

    @TableField(value = "reply_message")
    private String replyMessage;    // 批复内容

    @TableField("org_id")
    private String orgID; // 机构ID

    @TableField("org_name")
    private String orgName;    // 机构名称

    @TableField("org_type")
    private OrganizationType orgType;    // 机构类型

    @TableField("org_telephone")
    private String orgTelephone;   // 机构电话

    @TableField("org_email")
    private String orgEmail;   // 机构邮箱

    @TableField("org_city")
    private String orgCity;    // 机构城市

    @TableField("org_address")
    private String orgAddress; // 机构地址

    @TableField("org_introduction")
    private String orgIntroduction;    // 机构介绍

    @TableField("org_superior_id")
    private String orgSuperiorID; // 上级机构ID

    @TableField("org_creation_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date orgCreationTime;   // 创建时间

    @TableField("org_logo")
    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String orgLogo; // 机构Logo

    @TableField("org_file")
    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String orgFile;    // 机构文件
}
