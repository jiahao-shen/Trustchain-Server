package com.hawkeye.util;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.hawkeye.model.entity.User;

public class AuthUtil {
    public static void setUser(User user) {
        StpUtil.getSession().set("user", JSON.toJSONString(user));
    }

    public static User getUser() {
        return JSON.parseObject((String) StpUtil.getSession().get("user"), User.class);
    }

}
