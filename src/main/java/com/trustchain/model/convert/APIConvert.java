package com.trustchain.model.convert;

import com.trustchain.model.entity.APIRegister;
import com.trustchain.model.vo.APIRegisterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface APIConvert {
    APIConvert INSTANCE = Mappers.getMapper(APIConvert.class);

    APIRegisterVO toAPIRegisterVO(APIRegister apiRegister);

    List<APIRegisterVO> toAPIRegisterVOList(List<APIRegister> apiRegisterList);
}
