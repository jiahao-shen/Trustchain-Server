package com.trustchain.service;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.trustchain.convert.UserConvert;
import com.trustchain.mapper.UserMapper;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.UserRegister;
import com.trustchain.model.vo.UserInformationVO;
import com.trustchain.model.vo.UserLoginVO;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MinioService minioService;
    @Autowired
    private FabricService fabricService;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    public UserLoginVO login(String organization, String username, String password) {
        QueryWrapper query = QueryWrapper.create()
                .from(User.class)
                .where(User::getOrganizationID).eq(organization)
                .and(User::getUsername).eq(username);

        User user = userMapper.selectOneWithRelationsByQuery(query);

        if (user != null && PasswordUtil.match(password, user.getPassword())) {
            StpUtil.login(user.getId());
            return new UserLoginVO(UserConvert.INSTANCE.toUserInfoVO(user), StpUtil.getTokenInfo());
        }
        return null;
    }

    public void logout(String id) {
        StpUtil.logout(id);
    }

    public String registerApply(UserRegister userReg) {
        return null;
    }

    public String registerReply() {
        return null;
    }

    public Boolean register(User user) {
        int count = userMapper.insert(user);

        return count != 0;
    }
}
