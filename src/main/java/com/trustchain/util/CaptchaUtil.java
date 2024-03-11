package com.trustchain.util;

import com.trustchain.service.EmailSerivce;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类
 */
@Component
public class CaptchaUtil {
    @Autowired
    private EmailSerivce emailSerivce;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送验证码
     *
     * @param email: 目标邮箱
     * @return: 是否发送成功
     */
    public Boolean send(String email) {
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
     * 检查验证码
     *
     * @param email: 目标邮箱
     * @param code:  验证码
     * @return: 是否验证成功
     */
    public Boolean verify(String email, String code) {
        String value = redisTemplate.opsForValue().get("captcha:" + email);

        return code.equals(value);
    }
}
