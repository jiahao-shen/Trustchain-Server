package com.trustchain.mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trustchain.model.OrganizationRegister;

public interface OrganizationRegisterMapper extends BaseMapper<OrganizationRegister> {


//    @Select("select register with serialNumber")
//    OrganizationRegister getOrganizationRegisterInformation(@Param("serialNumber") Long serialNumber);
}
