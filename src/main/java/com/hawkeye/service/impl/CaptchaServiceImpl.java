package com.hawkeye.service.impl;

import com.hawkeye.exception.CaptchaException;
import com.hawkeye.mapper.OrganizationMapper;
import com.hawkeye.mapper.UserMapper;
import com.hawkeye.service.CaptchaService;
import com.hawkeye.service.EmailSerivce;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private EmailSerivce emailSerivce;
    @Autowired
    private OrganizationMapper orgMapper;
    @Autowired
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean send(String email) {
        // TODO: 开发调试用
        email = "1843781563@qq.com";
        String code = new PasswordGenerator().generatePassword(8,
                new CharacterRule(EnglishCharacterData.Digit),
                new CharacterRule(EnglishCharacterData.Alphabetical));

        // 5分钟后过期
        redisTemplate.opsForValue().set("captcha:" + email, code, 500, TimeUnit.MINUTES);

        return emailSerivce.send(email, "数据资源可信共享平台 邮箱验证码",
                "您的验证码如下, 有效期为5分钟。<br>" +
                        "<h3>" + code + "</h3>");
    }

    @Override
    public void verify(String email, String code) {
//        // TODO: 开发调用
//        email = "1843781563@qq.com";
//        // TODO: 开发调试用
//        String value = redisTemplate.opsForValue().get("captcha:" + email);
//
//        if (!code.equals(value)) {
//            throw new CaptchaException();
//        }
    }
}
