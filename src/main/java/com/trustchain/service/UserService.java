package com.trustchain.service;

import com.trustchain.enums.RegisterStatus;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserLogin;

import java.util.List;

public interface UserService {

    /**
     * @param orgId
     * @param username
     * @param password
     * @return
     */
    UserLogin login(String orgId, String username, String password);

    /**
     * @param userId
     */
    void logout(String userId);

    /**
     * @param ordId
     * @param username
     * @param password
     * @return
     */
    boolean resetPassword(String ordId, String username, String password);

    /**
     * @param userReg
     * @return
     */
    String registerApply(UserRegister userReg);

    /**
     * @param regIds
     * @return
     */
    List<UserRegister> registerApplySearch(List<String> regIds);

    /**
     * @return
     */
    boolean registerReply(String regId, RegisterStatus reply, String reason);

    /**
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * @param orgId
     * @return
     */
    List<UserRegister> registerList(String orgId);

    /**
     * @param regId
     * @return
     */
    UserRegister registerDetail(String regId);

    /**
     * @param orgId:    机构ID
     * @param username: 用户名
     * @param userId:   用户ID
     * @return: 是否存在
     */
    boolean exist(String orgId, String username, String userId);

    /**
     * @param orgId: 机构ID
     * @return: 用户列表
     */
    List<User> subordinateList(String orgId);

    /**
     * @param userId: 用户ID
     * @return: 用户详情
     */
    User subordinateDetail(String userId);

    /**
     * @param userId: 用户ID
     * @return: 用户详情
     */
    User informationDetail(String userId, String version);
}
