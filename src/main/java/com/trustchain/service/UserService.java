package com.trustchain.service;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.trustchain.convert.UserConvert;
import com.trustchain.mapper.UserMapper;
import com.trustchain.mapper.UserRegisterMapper;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserLogin;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRegisterMapper userRegMapper;
    @Autowired
    private MinioService minioService;
    @Autowired
    private FabricService fabricService;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    public UserLogin login(String organization, String username, String password) {
        QueryWrapper query = QueryWrapper.create()
                .from(User.class)
                .where(User::getOrganizationID).eq(organization)
                .and(User::getUsername).eq(username);

        User user = userMapper.selectOneWithRelationsByQuery(query);

        if (user != null && PasswordUtil.match(password, user.getPassword())) {
            StpUtil.login(user.getId());
            return new UserLogin(UserConvert.INSTANCE.toUserInformation(user), StpUtil.getTokenInfo());
        }
        return null;
    }

    public void logout(String id) {
        StpUtil.logout(id);
    }

    public String registerApply(UserRegister userReg) {
        int count = userRegMapper.insert(userReg);

        if (count != 0) {
            return userReg.getRegID();
        } else {
            return null;
        }
    }

    public List<UserRegister> registerApplySearch(List<String> regIDs) {
        return userRegMapper.selectListByIds(regIDs);
    }

    public String registerReply() {
        return null;
    }

    public Boolean register(User user) {
        int count = userMapper.insert(user);

        return count != 0;
    }

    public Boolean exist(String organization, String username, String id) {
        QueryWrapper query = QueryWrapper.create()
                .from(User.class)
                .where(User::getOrganizationID).eq(organization)
                .and(User::getUsername).eq(username)
                .and(User::getId).ne(id);

        User user = userMapper.selectOneByQuery(query);

        return user != null;
    }
}
