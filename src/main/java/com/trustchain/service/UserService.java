package com.trustchain.service;

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

    boolean resetPassword(String ordId, String username, String password);

    String registerApply(UserRegister userReg);

    /**
     * @param regIds
     * @return
     */
    List<UserRegister> registerApplySearch(List<String> regIds);

    /**
     * @return
     */
    String registerReply();

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
     *
     * @param regId
     * @return
     */
    UserRegister registerDetail(String regId);

    /**
     * @param orgId: 机构ID
     * @param username:     用户名
     * @param userId:           用户ID
     * @return
     */
    boolean exist(String orgId, String username, String userId);

}
