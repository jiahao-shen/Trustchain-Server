package com.trustchain.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.trustchain.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

public interface UserMapper extends BaseMapper<User> {

    @Select("select count(*) from user as usersum where organization = #{organizationId}")
    Integer userSum(@Param("organizationId") Long organizationId,
                    @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select count(*) from user as usersumoneday where created_time >= #{startTime} and " +
            "created_time < #{endTime} and organizationId = #{organizationId}")
    Integer userSumOneDay(@Param("startTime") Date startTime,
                          @Param("endTime") Date endTime,
                          @Param("organizationId") Long organizationId,
                          @Param(Constants.WRAPPER) Wrapper wrapper);

}
