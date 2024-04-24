package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.CertificateStatus;
import lombok.Data;

import java.util.Date;

@Data
@Table("certificate")
public class Certificate {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("id")
    @ColumnAlias("certificate_id")
    private String id;  // 合格证ID

    @Column("version")
    @ColumnAlias("certificate_version")
    private String version; // 合格证版本

    @Column("type")
    @ColumnAlias("certificate_type")
    private String type;

    @Column("production_unit")
    @ColumnAlias("certificate_production_unit")
    private String productionUnit;

    @Column(value = "production_date", onInsertValue = "now()")
    @ColumnAlias("certificate_production_date")
    private Date productionDate;

    @Column("check_standard")
    @ColumnAlias("certificate_check_standard")
    private String checkStandard;

    @Column("check_user")
    @ColumnAlias("certificate_check_user")
    private String checkUser;

    @Column(value = "check_date", onInsertValue = "now()")
    @ColumnAlias("certificate_check_date")
    private Date checkDate;

    @Column("status")
    @ColumnAlias("certificate_status")
    private CertificateStatus certificateStatus;

    @Column("part_list")
    @ColumnAlias("certificate_part_list")
    private String partList;

}
