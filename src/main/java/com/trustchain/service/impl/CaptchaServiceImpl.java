package com.trustchain.service.impl;

import com.trustchain.service.CaptchaService;
import com.trustchain.service.EmailSerivce;
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

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 向指定邮箱发送验证码
     *
     * @param email: 邮箱
     * @return: 是否发送成功
     */
    @Override
    public boolean send(String email) {
        email = "1843781563@qq.com"; // TODO: 开发调试用
        String code = new PasswordGenerator().generatePassword(8,
                new CharacterRule(EnglishCharacterData.Digit),
                new CharacterRule(EnglishCharacterData.Alphabetical));

        // 5分钟后过期
        redisTemplate.opsForValue().set("captcha:" + email, code, 5, TimeUnit.MINUTES);

        return emailSerivce.send(email, "数据资源可信共享平台 邮箱验证码",
                "您的验证码如下, 有效期为5分钟。<br>" +
                        "<h3>" + code + "</h3>");
    }

    /**
     * 验证码是否正确
     *
     * @param email: 邮箱
     * @param code:  验证码
     * @return: 是否正确
     */
    @Override
    public boolean verify(String email, String code) {
        email = "1843781563@qq.com";    // TODO: 开发调试用
        String value = redisTemplate.opsForValue().get("captcha:" + email);

        return code.equals(value);
    }
}
