package com.trustchain.model;

import com.trustchain.model.entity.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrganizationInfo extends Organization {
//    @TableField("superior_name")
//    private String superiorName;
}