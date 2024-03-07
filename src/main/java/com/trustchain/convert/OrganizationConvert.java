package com.trustchain.convert;

import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.OrganizationInformation;
import com.trustchain.model.vo.OrganizationRegisterInformation;
import com.trustchain.model.vo.OrganizationSelectItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrganizationConvert {
    OrganizationConvert INSTANCE = Mappers.getMapper(OrganizationConvert.class);

    Organization toOrganization(OrganizationRegister organizationRegister);

    OrganizationInformation toOrganizationInformation(Organization organization);

    OrganizationSelectItem toOrganizationSelectItem(Organization organization);

    List<OrganizationSelectItem> toOrganizationSelectItemList(List<Organization> organizationList);

    OrganizationRegisterInformation toOrganizationRegisterInformation(OrganizationRegister organizationRegister);

    List<OrganizationRegisterInformation> toOrganizationRegisterInformationList(List<OrganizationRegister> organizationRegisterList);
}
