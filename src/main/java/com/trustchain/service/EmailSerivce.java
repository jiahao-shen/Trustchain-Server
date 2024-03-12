package com.trustchain.service;

import com.trustchain.config.EmailConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailSerivce {
    @Autowired
    private JavaMailSender javaMailSender;

    private final EmailConfig config;

    private static final Logger logger = LogManager.getLogger(EmailSerivce.class);

    @Autowired
    EmailSerivce(EmailConfig config) {
        this.config = config;
    }

    /**
     * 发送邮件
     *
     * @param to:      接收人
     * @param subject: 邮件主题
     * @param text:    邮件正文
     * @return: 是否发送成功
     */
    public boolean send(String to, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(config.getUsername());
            // TODO: 正式版需要注释掉
            helper.setTo("1843781563@qq.com");
//            helper.setTo(to);

            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
