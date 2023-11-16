package com.trustchain.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.trustchain.model.API;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trustchain.model.APIInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface APIMapper extends BaseMapper<API> {
    @Select("select t1.*, t2.name as organization_name, t2.type as organization_type " +
            "from api as t1 left join organization as t2 on t1.organization=t2.id ${ew.customSqlSegment}")
    List<APIInfo> getAllAPIList(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select count(*) from api as apisum")
    Integer getApiSum(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select count(*) from api as apisumoneday where created_time" +
            " >= #{startTime} and created_time < #{endTime}")
    Integer getApiSumOneDay(@Param("startTime") Date startTime,
                            @Param("endTime") Date endTime,
                            @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select count(*) from api as apipercent where method = #{httpMethod}")
    Integer getApiOneTypeNum(@Param("httpMethod") int httpMethod,
                             @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select count(*) from api as apisumoneorganization where "+
            "organization = #{organization}")
    Integer getApiSumOneOrganizaiton(@Param("organization") Long organization,
                                     @Param(Constants.WRAPPER) Wrapper wrapper);

}
