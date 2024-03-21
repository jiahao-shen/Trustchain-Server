package com.trustchain.model.convert;

import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.OrganizationRegisterVO;
import com.trustchain.model.vo.OrganizationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrganizationConvert {
    OrganizationConvert INSTANCE = Mappers.getMapper(OrganizationConvert.class);

    Organization toOrganization(OrganizationRegister organizationRegister);

    OrganizationVO toOrganizationVO(Organization organization);

    List<OrganizationVO> toOrganizationVOList(List<Organization> organizationList);

    OrganizationRegisterVO toOrganizationRegisterVO(OrganizationRegister organizationRegister);

    List<OrganizationRegisterVO> toOrganizationRegisterVOList(List<OrganizationRegister> organizationRegisterList);
}
