package com.trustchain.service;

import com.mybatisflex.core.paginate.Page;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserLogin;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * @param orgId    机构ID
     * @param username 用户名
     * @param password 密码
     * @return 用户信息和Token
     */
    UserLogin login(String orgId, String username, String password);

    /**
     * @param userId 用户ID
     */
    void logout(String userId);

    /**
     * @param orgId    机构ID
     * @param username 用户名
     * @param userId   用户ID
     * @return 是否存在
     */
    boolean exist(String orgId, String username, String userId);

    /**
     * @param user 用户
     * @return 是否成功
     */
    boolean resetPassword(User user, String password);

    /**
     * @param userReg 用户注册对象
     * @return 注册申请号
     */
    String registerApply(UserRegister userReg);

    /**
     * @param applyIds 注册申请号列表
     * @return 用户注册申请列表
     */
    List<UserRegister> registerApplyList(List<String> applyIds);

    /**
     * @param applyIds   注册申请号
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return 用户注册申请列表
     */
    Page<UserRegister> registerApplyList(List<String> applyIds,
                                         Integer pageNumber,
                                         Integer pageSize,
                                         Map<String, List<String>> filter,
                                         Map<String, String> sort);

    /**
     * @param applyId 注册申请号
     * @return 用户注册申请详情
     */
    UserRegister registerApplyDetail(String applyId);

    /**
     * @param applyId 注册申请号
     * @param reply   回复
     * @param reason  回复理由
     */
    void registerReply(String applyId, ApplyStatus reply, String reason);

    /**
     * @param user 用户对象
     * @return 是否注册成功
     */
    boolean register(User user);

    /**
     * @param orgId 机构ID
     * @return 用户注册申请列表
     */
    List<UserRegister> registerApprovalList(String orgId);

    /**
     * @param orgId      机构ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return 用户注册申请列表
     */
    Page<UserRegister> registerApprovalList(String orgId,
                                            Integer pageNumber,
                                            Integer pageSize,
                                            Map<String, List<String>> filter,
                                            Map<String, String> sort);

    /**
     * @param applyId 注册申请号
     * @return 用户注册申请详情
     */
    UserRegister registerDetail(String applyId);

    /**
     * @param orgId 机构ID
     * @return 用户列表
     */
    List<User> subordinateList(String orgId);

    /**
     * @param orgId      机构ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return 用户列表
     */
    Page<User> subordinateList(String orgId,
                               Integer pageNumber,
                               Integer pageSize,
                               Map<String, List<String>> filter,
                               Map<String, String> sort);


    /**
     * @param userId 用户ID
     * @return 用户详情
     */
    User subordinateDetail(String userId);

    /**
     * @param userId 用户ID
     * @return 用户详情
     */
    User informationDetail(String userId, String version);

    /**
     * @param user 用户
     * @return 更新后的用户
     */
    User informationUpdate(User user);

    /**
     * @param userId 用户ID
     * @return 页数
     */
    List<User> informationHistory(String userId);

    /**
     * @param userId     用户ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return 用户信息历史记录
     */
    Page<User> informationHistory(String userId,
                                  Integer pageNumber,
                                  Integer pageSize,
                                  Map<String, List<String>> filter,
                                  Map<String, String> sort);

    /**
     * @param userId  用户ID
     * @param version 版本
     * @return 是否回滚成功
     */
    boolean informationRollback(String userId, String version);

    /**
     * @param orgId    机构ID
     * @param username 用户名
     * @return 用户
     */
    User findByUsername(String orgId, String username);

}
