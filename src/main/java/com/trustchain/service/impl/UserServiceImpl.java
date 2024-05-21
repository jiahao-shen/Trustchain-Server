package com.trustchain.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.mapper.*;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.model.dto.UserDTO;
import com.trustchain.model.entity.*;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.convert.UserConvert;
import com.trustchain.model.enums.UserRole;
import com.trustchain.model.vo.UserLogin;
import com.trustchain.service.*;
import com.trustchain.util.AuthUtil;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ResultOuterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.trustchain.model.entity.table.OrganizationTableDef.ORGANIZATION;
import static com.trustchain.model.entity.table.UserRegisterTableDef.USER_REGISTER;
import static com.trustchain.model.entity.table.UserTableDef.USER;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRegisterMapper userRegMapper;
    @Autowired
    private OrganizationMapper orgMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private MinioService minioService;
    @Autowired
    private EmailSerivce emailSerivce;
    @Autowired
    private ChainService chainService;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public UserLogin login(String orgId, String username, String password) {
        User user = findByUsername(orgId, username);

        if (user != null && PasswordUtil.match(password, user.getPassword())) {
            // SA-Token登录并缓存数据
            StpUtil.login(user.getId());
            AuthUtil.setUser(user);

            return new UserLogin(UserConvert.INSTANCE.userToUserVO(user), StpUtil.getTokenInfo());
        } else {
            return null;
        }
    }

    @Override
    public void logout(String userId) {
        StpUtil.logout(userId);
    }

    @Override
    public boolean resetPassword(User user, String password) {
        user.setPassword(PasswordUtil.encrypt(password));
        user.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());

        if (userMapper.update(user, true) != 0) {
            user = userMapper.selectOneById(user.getId());
            chainService.putState(user.getId(), "user", JSON.toJSONString(user), user.getVersion());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String registerApply(UserRegister userReg) {
        int count = userRegMapper.insert(userReg);

        if (count != 0) {
            emailSerivce.send(userReg.getEmail(),
                    "数据资源可信共享平台 注册申请",
                    "欢迎您注册数据资源可信共享平台, 您的注册申请号如下。<br>" + "<h3>" + userReg.getApplyId() + "</h3>");

            // 将Logo移至新目录
            String oldLogoPath = userReg.getLogo();
            String newLogoPath = "user_register/" + userReg.getId() + "/" + oldLogoPath.substring(oldLogoPath.lastIndexOf("/") + 1);
            minioService.copy(oldLogoPath, newLogoPath);

            userReg.setLogo(newLogoPath);
            userRegMapper.update(userReg, true);

            return userReg.getApplyId();
        } else {
            return null;
        }
    }

    @Override
    public List<UserRegister> registerApplyList(List<String> applyIds) {
        QueryWrapper query = QueryWrapper.create()
                .select(USER_REGISTER.APPLY_ID,
                        USER_REGISTER.USERNAME,
                        ORGANIZATION.NAME,
                        USER_REGISTER.APPLY_STATUS,
                        USER_REGISTER.APPLY_TIME,
                        USER_REGISTER.REPLY_TIME)
                .from(USER_REGISTER)
                .leftJoin(ORGANIZATION).on(ORGANIZATION.ID.eq(USER_REGISTER.ORGANIZATION_ID))
                .where(USER_REGISTER.APPLY_ID.in(applyIds));

        return userRegMapper.selectListByQuery(query);
    }

    @Override
    public Page<UserRegister> registerApplyList(List<String> applyIds,
                                                Integer pageNumber,
                                                Integer pageSize,
                                                Map<String, List<String>> filter,
                                                Map<String, String> sort) {

        QueryWrapper query = QueryWrapper.create()
                .select(USER_REGISTER.APPLY_ID,
                        USER_REGISTER.USERNAME,
                        ORGANIZATION.NAME,
                        USER_REGISTER.APPLY_STATUS,
                        USER_REGISTER.APPLY_TIME,
                        USER_REGISTER.REPLY_TIME)
                .from(USER_REGISTER)
                .leftJoin(ORGANIZATION).on(ORGANIZATION.ID.eq(USER_REGISTER.ORGANIZATION_ID))
                .where(USER_REGISTER.APPLY_ID.in(applyIds));

        filter.forEach((key, value) -> {
        });

        if (sort.isEmpty()) {
            query.orderBy(USER_REGISTER.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
            });
        }

        return userRegMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public UserRegister registerApplyDetail(String applyId) {
        RelationManager.setMaxDepth(1);
        return userRegMapper.selectOneWithRelationsById(applyId);
    }

    @Override
    @Transactional
    public void registerReply(String applyId, ApplyStatus reply, String reason) {
        UserRegister userReg = userRegMapper.selectOneById(applyId);
        if (userReg == null) {
            throw new RuntimeException("用户注册申请不存在");
        }
        if (reply == ApplyStatus.ALLOW) {
            User user = UserConvert.INSTANCE.userRegToUser(userReg);
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));

            // 移动Logo
            String oldLogoPath = userReg.getLogo();
            String newLogoPath = "user/" + user.getId() + "/" + oldLogoPath.substring(oldLogoPath.lastIndexOf("/") + 1);
            minioService.copy(oldLogoPath, newLogoPath);

            user.setLogo(newLogoPath);

            // 设置版本
            user.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());

            // 插入新机构
            userMapper.insert(user);
            user = userMapper.selectOneById(user.getId());
            // 写入链
            chainService.putState(user.getId(), "user", JSON.toJSONString(user), user.getVersion());

            // 创建钱包
            Wallet wallet = new Wallet();
            wallet.setUserId(user.getId());
            walletMapper.insert(wallet);

            userReg.setId(user.getId());
            userReg.setApplyStatus(ApplyStatus.ALLOW);
            userReg.setReplyTime(new Date());

            userRegMapper.update(userReg);

            emailSerivce.send(user.getEmail(),
                    "数据资源可信共享平台 注册成功",
                    "您的用户注册申请已通过, 请点击以下链接进行登录。<br>" + "<a>http://localhost:5173</a>");
        } else if (reply == ApplyStatus.REJECT) {
            userReg.setApplyStatus(ApplyStatus.REJECT);
            userReg.setReplyTime(new Date());
            userReg.setReplyReason(reason);

            userRegMapper.update(userReg);

            emailSerivce.send(userReg.getEmail(),
                    "数据资源可信共享平台 注册失败",
                    "您的用户注册申请未通过, 请点击以下链接查看详情。<br>" + "<a>http://localhost:5173/registerApplySearch</a>");
        }
    }

    @Override
    @Transactional
    public boolean register(User user) {
        user.setId(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        user.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());

        // 将机构的Logo作为当前用户的Logo
        Organization org = orgMapper.selectOneById(user.getOrganizationId());
        String oldLogoPath = org.getLogo();
        String newLogoPath = "user/" + user.getId() + "/" + oldLogoPath.substring(oldLogoPath.lastIndexOf("/") + 1);
        minioService.copy(oldLogoPath, newLogoPath);
        user.setLogo(newLogoPath);

        // 插入新用户
        userMapper.insert(user);
        user = userMapper.selectOneById(user.getId());
        // 写入链
        chainService.putState(user.getId(), "user", JSON.toJSONString(user), user.getVersion());

        // 生成钱包
        Wallet wallet = new Wallet();
        wallet.setUserId(user.getId());
        walletMapper.insert(wallet);

        return true;
    }

    @Override
    public List<UserRegister> registerApprovalList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .select(USER_REGISTER.APPLY_ID,
                        USER_REGISTER.USERNAME,
                        USER_REGISTER.EMAIL,
                        USER_REGISTER.APPLY_STATUS,
                        USER_REGISTER.APPLY_TIME)
                .from(USER_REGISTER)
                .orderBy(USER_REGISTER.APPLY_TIME, false);

        return userRegMapper.selectListByQuery(query);
    }

    @Override
    public Page<UserRegister> registerApprovalList(String orgId,
                                                   Integer pageNumber,
                                                   Integer pageSize,
                                                   Map<String, List<String>> filter,
                                                   Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .select(USER_REGISTER.APPLY_ID,
                        USER_REGISTER.USERNAME,
                        USER_REGISTER.EMAIL,
                        USER_REGISTER.APPLY_STATUS,
                        USER_REGISTER.APPLY_TIME)
                .from(USER_REGISTER)
                .where(USER_REGISTER.ORGANIZATION_ID.eq(orgId));

        filter.forEach((key, value) -> {
            switch (key) {
                case "applyStatus": {
                    query.where(USER_REGISTER.APPLY_STATUS.in(value.stream().map(ApplyStatus::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(USER_REGISTER.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "applyTime": {
                        query.orderBy(USER_REGISTER.APPLY_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return userRegMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public UserRegister registerDetail(String applyId) {
        RelationManager.setMaxDepth(1);
        return userRegMapper.selectOneWithRelationsById(applyId);
    }


    @Override
    public boolean exist(String orgId, String username, String userId) {
        QueryWrapper query = QueryWrapper.create()
                .from(USER)
                .where(USER.ORGANIZATION_ID.eq(orgId))
                .and(USER.USERNAME.eq(username))
                .and(USER.ID.ne(userId));

        User user = userMapper.selectOneByQuery(query);
        logger.info(user);

        return user != null;
    }

    @Override
    public List<User> subordinateList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .select(USER.ID,
                        USER.USERNAME,
                        USER.EMAIL,
                        USER.ROLE,
                        USER.REGISTRATION_TIME)
                .from(USER)
                .where(USER.ORGANIZATION_ID.eq(orgId))
                .orderBy(USER.REGISTRATION_TIME, false);

        return userMapper.selectListByQuery(query);
    }

    @Override
    public Page<User> subordinateList(String orgId,
                                      Integer pageNumber,
                                      Integer pageSize,
                                      Map<String, List<String>> filter,
                                      Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .select(USER.ID,
                        USER.USERNAME,
                        USER.EMAIL,
                        USER.ROLE,
                        USER.REGISTRATION_TIME)
                .from(USER)
                .where(USER.ORGANIZATION_ID.eq(orgId));

        filter.forEach((key, value) -> {
            switch (key) {
                case "role": {
                    query.where(USER.ROLE.in(value.stream().map(UserRole::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(USER.REGISTRATION_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "registrationTime": {
                        query.orderBy(USER.REGISTRATION_TIME, "ascending".equals(value));
                    }
                }
            });
        }
        return userMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public User subordinateDetail(String userId) {
        return userMapper.selectOneById(userId);
    }

    @Override
    public UserDTO informationDetail(String userId, String version) {
        UserDTO userDTO;

        RelationManager.setMaxDepth(1);
        User latest = userMapper.selectOneWithRelationsById(userId);

        if (version.equals("@latest")) {
            // TODO: 链上版本和数据库对比
            userDTO = UserConvert.INSTANCE.userToUserDTO(latest);
        } else {
            userDTO = JSON.parseObject(chainService.getState(version), UserDTO.class);
            Organization org = orgMapper.selectOneById(userDTO.getOrganizationId());
            userDTO.setOrganization(OrganizationConvert.INSTANCE.orgToOrgDTO(org));
        }

        if (userDTO == null) {
            userDTO.setLatest(true);
        } else {
            if (userDTO.getLatest() != null && latest.getVersion() != null) {
                userDTO.setLatest(userDTO.getVersion().equals(latest.getVersion()));
            } else {
                userDTO.setLatest(true);
            }
        }

        return userDTO;
    }

    @Override
    public User informationUpdate(User user) {
        String logo = user.getLogo();
        if (!minioService.isUrl(logo)) {
            String newLogoPath = "user/" + user.getId() + "/" + logo.substring(logo.lastIndexOf("/") + 1);
            minioService.copy(logo, newLogoPath);
            user.setLogo(newLogoPath);
        } else {
            user.setLogo(null);
        }
        user.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());

        userMapper.update(user, true);
        user = userMapper.selectOneById(user.getId());

        chainService.putState(user.getId(), "user", JSON.toJSONString(user), user.getVersion());

        // TODO:
        RelationManager.setMaxDepth(1);
        return userMapper.selectOneWithRelationsById(user.getId());
    }

    @Override
    public List<UserDTO> informationHistory(String userId) {
        User latest = userMapper.selectOneById(userId);

        List<UserDTO> users = new ArrayList<>();

        JSONArray histories = JSON.parseArray(chainService.getHistory(userId, "user"));

        histories.forEach(item -> {
            JSONObject tmp = (JSONObject) item;
            UserDTO user = JSON.parseObject(tmp.getString("value"), UserDTO.class);
            user.setLatest(user.getVersion().equals(latest.getVersion()));
            users.add(user);
        });

        users.sort(Comparator.comparing(UserDTO::getLastModified).reversed());

        return users;
    }

    @Override
    public boolean informationRollback(String userId, String version) {
        // TODO: 对接长安链
        return false;
    }

    @Override
    public User findByUsername(String orgId, String username) {
        QueryWrapper query = QueryWrapper.create()
                .from(USER)
                .where(USER.ORGANIZATION_ID.eq(orgId))
                .and(USER.USERNAME.eq(username));

        RelationManager.setMaxDepth(1);
        return userMapper.selectOneWithRelationsByQuery(query);
    }
}
