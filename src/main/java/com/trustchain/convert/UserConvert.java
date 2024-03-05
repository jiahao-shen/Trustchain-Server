package com.trustchain.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.trustchain.model.entity.User;
import com.trustchain.model.vo.UserInformationVO;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserInformationVO toUserInfoVO(User user);
}
