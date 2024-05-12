package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.CertificateStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Certificate {
    private String id;  // 合格证标识符

    private String productionId;    // 产品编号

    private String productionType;  // 产品型号

    private Date productionDate;    // 生产日期

    private String currentState;    // 当前状态

    private List<String> fuck;
}
