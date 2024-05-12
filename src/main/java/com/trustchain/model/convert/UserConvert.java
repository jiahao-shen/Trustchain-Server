package com.trustchain.model.convert;

import com.trustchain.model.dto.UserDTO;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserRegisterVO;
import com.trustchain.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.trustchain.model.entity.User;

import java.util.List;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    User userRegToUser(UserRegister userRegister);

    UserDTO userToUserDTO(User user);

    List<UserDTO> userListToUserDTOList(List<User> userList);

    UserVO userDTOToUserVO(UserDTO userDTO);

    List<UserVO> userDTOListToUserVOList(List<UserDTO> userDTOList);

    UserVO userToUserVO(User user);

    List<UserVO> userListToUserVOList(List<User> userList);

    UserRegisterVO userRegToUserRegVO(UserRegister userRegister);

    List<UserRegisterVO> userRegListToUserRegVOList(List<UserRegister> userRegisterList);
}
