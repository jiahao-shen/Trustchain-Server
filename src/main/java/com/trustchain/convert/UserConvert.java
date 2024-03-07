package com.trustchain.convert;

import com.trustchain.model.vo.UserRegisterInformation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.trustchain.model.entity.User;
import com.trustchain.model.vo.UserInformation;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserInformation toUserInformation(User user);

//    UserRegisterInformation
}
