package com.trustchain.service;

import org.bouncycastle.asn1.cmp.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    public JavaMailSender javaMailSender;// 从容器中拿到 邮件发送对象

    @Value("${spring.mail.from}")
    private String emailFrom;// 从资源文件中进行读取发送方的邮件地址

    public Boolean send(String toEmail, String text, String subject) {
        // new一个 简单邮件对象
        //TODO: cannot send email to XX@buaa.edu.cn

        SimpleMailMessage mail = new SimpleMailMessage();

        String vcode = verifycode();

        // 设置邮件对象的各个属性，构造成一个较为完整的邮件对象

        mail.setFrom(emailFrom);// 发邮件的邮箱地址，从资源可直接提取
        mail.setTo(toEmail);// 给谁发，通过参数进行传递
        mail.setText(text);// 设置邮件的文本内容
        mail.setSubject(subject);// 设置邮件的主题
        try{
            javaMailSender.send(mail);// 进行发送
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public String verifycode(){
        Random random = new Random();
        String vcode = "";
        for (int i=0;i<6;i++){
            vcode += random.nextInt(10);
        }
        return vcode;
    }


}
