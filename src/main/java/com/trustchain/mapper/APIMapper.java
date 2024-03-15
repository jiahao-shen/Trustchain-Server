package com.trustchain.mapper;

import com.mybatisflex.core.BaseMapper;
import com.trustchain.model.entity.API;

//public interface APIMapper extends BaseMapper<API> {
//    @Select("select t1.*, t2.name as organization_name, t2.type as organization_type " +
//            "from api as t1 left join organization as t2 on t1.organization=t2.id ${ew.customSqlSegment}")
//    List<APIInfo> getAllAPIList(@Param(Constants.WRAPPER) Wrapper wrapper);
//
//}
public interface APIMapper extends BaseMapper<API> {
}
