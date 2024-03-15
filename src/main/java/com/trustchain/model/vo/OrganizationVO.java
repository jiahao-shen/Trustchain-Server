package com.trustchain.model.vo;


import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.OrganizationType;
import com.trustchain.model.serializer.MinioURLSerializer;
import com.alibaba.fastjson2.JSONWriter.Feature;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationVO {

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

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;   // 注册时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;   // 最后修改时间

    private String version;  // 版本号

    private boolean latest; // 是否为最新

    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String logo; // 机构Logo

    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String file;    // 机构文件
}