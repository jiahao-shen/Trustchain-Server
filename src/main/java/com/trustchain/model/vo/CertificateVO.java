package com.trustchain.model.vo;

import com.trustchain.model.enums.CertificateStatus;
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

    private Date productionDate;

    private String checkStandard;

    private String checkUser;

    private Date checkDate;

    private CertificateStatus certificateStatus;

    private String partList;
}
