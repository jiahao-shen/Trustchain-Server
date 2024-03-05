package com.trustchain.mapper;

import com.mybatisflex.core.BaseMapper;
import com.trustchain.model.entity.Organization;

public interface OrganizationMapper extends BaseMapper<Organization> {

//    @Select("select t1.*, t2.name as superior_name from (select * from organization where id=#{id}) as t1 " +
//            "left join organization as t2 on t1.superior=t2.id")
//    OrganizationInfo getOrganizationInformation(@Param("id") Long id);

}
