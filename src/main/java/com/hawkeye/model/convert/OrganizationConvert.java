package com.hawkeye.model.convert;

import com.hawkeye.model.dto.OrganizationDTO;
import com.hawkeye.model.entity.Organization;
import com.hawkeye.model.entity.OrganizationRegister;
import com.hawkeye.model.vo.OrganizationRegisterVO;
import com.hawkeye.model.vo.OrganizationVO;
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
