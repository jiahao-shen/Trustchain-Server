package com.trustchain.model.convert;

import com.trustchain.model.dto.OrganizationDTO;
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

    Organization orgRegToOrg(OrganizationRegister organizationRegister);

    OrganizationDTO orgToOrgDTO(Organization org);

    OrganizationVO orgToOrgVO(Organization organization);

    OrganizationVO orgDTOToOrgVO(OrganizationDTO organizationDTO);

    List<OrganizationVO> orgListToOrgVOList(List<Organization> organizationList);

    List<OrganizationVO> orgDTOListToOrgVOList(List<OrganizationDTO> organizationDTOList);

    OrganizationRegisterVO orgRegToOrgRegVO(OrganizationRegister organizationRegister);

    List<OrganizationRegisterVO> orgRegListToOrgRegVOList(List<OrganizationRegister> organizationRegisterList);
}
