package com.trustchain.service;

import com.trustchain.model.enums.RegisterStatus;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserLogin;

import java.util.List;

public interface UserService {

    /**
     * @param orgId:    机构ID
     * @param username: 用户名
     * @param password: 密码
     * @return: 用户信息和Token
     */
    UserLogin login(String orgId, String username, String password);

    /**
     * @param userId: 用户ID
     */
    void logout(String userId);

    /**
     * @param orgId:    机构ID
     * @param username: 用户名
     * @param userId:   用户ID
     * @return: 是否存在
     */
    boolean exist(String orgId, String username, String userId);

    /**
     * @param user:     用户对象
     * @param password: 密码
     * @return: 是否成功
     */
    boolean resetPassword(User user, String password);

    /**
     * @param userReg: 用户注册对象
     * @return: 注册ID
     */
    String registerApply(UserRegister userReg);

    /**
     * @param regIds: 注册ID
     * @return: 用户注册申请
     */
    List<UserRegister> registerApplySearch(List<String> regIds);

    /**
     * @param regId:  注册ID
     * @param reply:  回复
     * @param reason: 回复理由
     * @return
     */
    boolean registerReply(String regId, RegisterStatus reply, String reason);

    /**
     * @param user: 用户对象
     * @return
     */
    boolean register(User user);

    /**
     * @param orgId: 机构ID
     * @return
     */
    List<UserRegister> registerList(String orgId);

    /**
     * @param regId: 注册ID
     * @return
     */
    UserRegister registerDetail(String regId);

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

    /**
     * @param user: 用户
     * @return
     */
    User informationUpdate(User user);

    /**
     * @param userId
     * @return
     */
    List<User> informationHistory(String userId);

    /**
     * @param userId
     * @param version
     * @return
     */
    boolean informationRollback(String userId, String version);

    /**
     * @param orgId
     * @param username
     * @return
     */
    User findByUsername(String orgId, String username);
}
