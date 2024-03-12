package com.trustchain.model.convert;

import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserRegisterInformation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.trustchain.model.entity.User;
import com.trustchain.model.vo.UserInformation;

import java.util.List;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserInformation toUserInformation(User user);

    UserRegisterInformation toUserRegisterInformation(UserRegister userRegister);

    List<UserRegisterInformation> toUserRegisterInformationList(List<UserRegister> userRegisterList);
}
