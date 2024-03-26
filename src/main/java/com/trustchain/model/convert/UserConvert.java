package com.trustchain.model.convert;

import com.trustchain.model.entity.Transaction;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.entity.Wallet;
import com.trustchain.model.vo.TransactionVO;
import com.trustchain.model.vo.UserRegisterVO;
import com.trustchain.model.vo.UserVO;
import com.trustchain.model.vo.WalletVO;
import org.checkerframework.checker.units.qual.A;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.trustchain.model.entity.User;

import java.util.List;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    User toUser(UserRegister userRegister);

    UserVO toUserVO(User user);

    List<UserVO> toUserVOList(List<User> userList);

    UserRegisterVO toUserRegisterVO(UserRegister userRegister);

    List<UserRegisterVO> toUserRegisterVOList(List<UserRegister> userRegisterList);
}
