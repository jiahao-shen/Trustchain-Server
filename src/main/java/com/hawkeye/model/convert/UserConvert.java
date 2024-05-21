package com.hawkeye.model.convert;

import com.hawkeye.model.dto.UserDTO;
import com.hawkeye.model.entity.UserRegister;
import com.hawkeye.model.vo.UserRegisterVO;
import com.hawkeye.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.hawkeye.model.entity.User;

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
