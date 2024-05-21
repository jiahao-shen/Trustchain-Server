package com.hawkeye.model.dto;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {
    private String productionId;  // 产品编号

    private String productionModel;  // 产品型号

    private String productionDepartment;   // 生产部门

    private Date productionDate;    // 生产日期

    private String state;    // 当前状态

    private List<JSONObject> inspection; // 检验标准

    private List<JSONObject> componentList; // 零件列表

    private Date creationTime;  // 合格证生成时间

    private Date lastModified;  // 最后更新时间

    private String version; // 版本号

    private Boolean latest; // 是否最新
}
