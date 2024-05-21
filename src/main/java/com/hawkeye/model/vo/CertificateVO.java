package com.hawkeye.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.CertificateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateVO {
    private String id;  // 合格证ID

    private String version; // 合格证版本

    private String type;

    private String productionUnit;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date productionDate;

    private String checkStandard;

    private String checkUser;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date checkDate;

    private CertificateStatus certificateStatus;

    private String partList;
}
