package com.trustchain.model;

import com.trustchain.enums.OrganizationType;
import com.trustchain.model.entity.API;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class APIInfo extends API {
    private String organizationName;

    private OrganizationType organizationType;
}
