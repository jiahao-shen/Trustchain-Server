package com.trustchain.model;

import com.trustchain.enums.OrganizationType;
import com.trustchain.minio.MinioURLSerializer;
import lombok.Data;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.alibaba.fastjson.serializer.ToStringSerializer;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("organization")
public class Organization {
    @TableId(value = "org_id", type = IdType.ASSIGN_UUID)
    private String orgID;  // 机构ID

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

    @TableField("org_registration_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date orgRegistrationTime;   // 注册时间

    @TableField("org_last_modified")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date orgLastModified;   // 最后更新时间

    @TableField("org_version")
    private String orgVersion;  // 版本号

    @TableField("org_logo")
    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String orgLogo; // 机构Logo

    @TableField("org_file")
    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String orgFile;    // 机构文件
}
