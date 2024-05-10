package com.trustchain.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.OrganizationType;
import com.trustchain.model.vo.OrganizationVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {

    private String id;  // 机构ID

    private String name;    // 机构名称

    private OrganizationType type;    // 机构类型

    private String telephone;   // 机构电话

    private String email;   // 机构邮箱

    private String city;    // 机构城市

    private String address; // 机构地址

    private String introduction;    // 机构介绍

    private String superiorId;  // 上级机构ID

    private OrganizationDTO superior; // 上级机构

    private Date creationTime;   // 创建时间

    private Date registrationTime;   // 注册时间

    private Date lastModified;   // 最后修改时间

    private String version;  // 版本号

    private Boolean latest; // 是否为最新

    private String logo; // 机构Logo

    private String file;    // 机构文件
}
