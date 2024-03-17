package com.trustchain.model.convert;

import com.trustchain.model.entity.ApiRegister;
import com.trustchain.model.vo.ApiRegisterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ApiConvert {
    ApiConvert INSTANCE = Mappers.getMapper(ApiConvert.class);

    ApiRegisterVO toAPIRegisterVO(ApiRegister apiRegister);

    List<ApiRegisterVO> toAPIRegisterVOList(List<ApiRegister> apiRegisterList);
}
