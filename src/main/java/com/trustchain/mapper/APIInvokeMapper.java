package com.trustchain.mapper;

//public interface APIInvokeMapper extends BaseMapper<APIInvoke> {
//
//    @Select("select t1.*, t2.name as api_name, t3.name as organization_name " +
//            "from (select * from api_invoke where applicant=#{applicant}) as t1 " +
//            "left join api as t2 on t1.id=t2.id " +
//            "left join organization as t3 on t2.organization=t3.id ${ew.customSqlSegment}")
//    List<APIInvokeApplyInfo> getAPIInvokeApplyList(@Param("applicant") Long applicant,
//                                                   @Param(Constants.WRAPPER) Wrapper wrapper);
//
//
//    @Select("select t1.*, t2.name as api_name, t3.username as applicant_name " +
//            "from (select * from api_invoke where author=#{author}) as t1 " +
//            "left join api as t2 on t1.id=t2.id " +
//            "left join user as t3 on t1.applicant=t3.id ${ew.customSqlSegment}")
//    List<APIInvokeApprovalInfo> getAPIInvokeApprovalList(@Param("author") Long author,
//                                                         @Param(Constants.WRAPPER) Wrapper wrapper);
//}

public interface APIInvokeMapper {
}
