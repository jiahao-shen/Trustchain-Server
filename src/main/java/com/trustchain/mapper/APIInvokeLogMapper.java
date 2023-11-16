package com.trustchain.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.trustchain.model.APIInvokeLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface APIInvokeLogMapper extends BaseMapper<APIInvokeLog> {

    @Select("select count(*) from api_invoke_log as apiinvokesum where applicant=#{applicant}")
    Integer apiInvokeSum(@Param("applicant") Long applicant,
                         @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select count(*) from api_invoke_log as apiinvokeoneday where invoke_time >= #{startTime} and " +
            "invoke_time < #{endTime} and applicant = #{applicant}")
    Integer apiInvokeSumOneDay(@Param("startTime") Date startTime,
                          @Param("endTime") Date endTime,
                          @Param("applicant") Long applicant,
                          @Param(Constants.WRAPPER) Wrapper wrapper);


}
