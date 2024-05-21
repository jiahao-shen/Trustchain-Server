package com.hawkeye.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.OrganizationType;
import com.hawkeye.model.enums.ApplyStatus;
import com.hawkeye.model.serializer.MinioUrlSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRegisterVO {
    private String applyId;  // 注册ID

    private ApplyStatus applyStatus;  // 申请状态

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;    // 申请时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;   // 批复时间

    private String replyReason;    // 批复内容

    private String id;  // 机构ID

    private String name;    // 机构名称

    private OrganizationType type;    // 机构类型

    private String telephone;   // 机构电话

    private String email;   // 机构邮箱

    private String city;    // 机构城市

    private String address; // 机构地址

    private String introduction;    // 机构介绍

    private OrganizationVO superior; // 上级机构

    @JSONField(format = "yyyy-MM-dd")
    private Date creationTime;   // 创建时间

    @JSONField(serializeUsing = MinioUrlSerializer.class)
    private String logo; // 机构Logo

    @JSONField(serializeUsing = MinioUrlSerializer.class)
    private String file;    // 机构文件
}
