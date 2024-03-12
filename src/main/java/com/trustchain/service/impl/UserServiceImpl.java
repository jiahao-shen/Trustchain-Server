package com.trustchain.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.mybatisflex.core.update.UpdateWrapper;
import com.trustchain.model.convert.UserConvert;
import com.trustchain.mapper.UserMapper;
import com.trustchain.mapper.UserRegisterMapper;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserLogin;
import com.trustchain.service.FabricService;
import com.trustchain.service.MinioService;
import com.trustchain.service.UserService;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRegisterMapper userRegMapper;
    @Autowired
    private MinioService minioService;
    @Autowired
    private FabricService fabricService;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserLogin login(String orgId, String username, String password) {
        QueryWrapper query = QueryWrapper.create()
                .from(User.class)
                .where(User::getOrganizationId).eq(orgId)
                .and(User::getUsername).eq(username);

        User user = userMapper.selectOneWithRelationsByQuery(query);

        if (user != null && PasswordUtil.match(password, user.getPassword())) {
            // SA-Token登录并缓存数据
            StpUtil.login(user.getId());
            StpUtil.getSession().set("user", user);
            return new UserLogin(UserConvert.INSTANCE.toUserInformation(user), StpUtil.getTokenInfo());
        }
        return null;
    }

    public void logout(String userId) {
        StpUtil.logout(userId);
    }

    @Override
    public boolean resetPassword(String ordId, String username, String password) {
        QueryWrapper query = QueryWrapper.create()
                .from(User.class)
                .where(User::getOrganizationId).eq(ordId)
                .and(User::getUsername).eq(username);

        User user = userMapper.selectOneByQuery(query);
        user.setPassword(PasswordUtil.encrypt(password));

        return userMapper.update(user) != 0;
    }

    public String registerApply(UserRegister userReg) {
        int count = userRegMapper.insert(userReg);

        if (count != 0) {
            return userReg.getRegId();
        } else {
            return null;
        }
    }

    public List<UserRegister> registerApplySearch(List<String> regIds) {
        List<UserRegister> userRegs = new ArrayList<UserRegister>();
        regIds.forEach(regId -> {
            userRegs.add(userRegMapper.selectOneWithRelationsById(regId));
        });

        logger.info(userRegs);

        return userRegs;
    }

    public String registerReply() {
        return null;
    }

    public boolean register(User user) {
        int count = userMapper.insert(user);

        return count != 0;
    }

    public List<UserRegister> registerList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(UserRegister.class)
                .where(UserRegister::getOrganizationId).eq(orgId);

        return userRegMapper.selectListByQuery(query);
    }

    @Override
    public UserRegister registerDetail(String regId) {
        RelationManager.setMaxDepth(1);
        return userRegMapper.selectOneWithRelationsById(regId);
    }

    public boolean exist(String orgId, String username, String userId) {
        QueryWrapper query = QueryWrapper.create()
                .from(User.class)
                .where(User::getOrganizationId).eq(orgId)
                .and(User::getUsername).eq(username)
                .and(User::getId).ne(userId);

        User user = userMapper.selectOneByQuery(query);

        return user != null;
    }
}
