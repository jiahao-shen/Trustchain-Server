package com.trustchain.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class EmailSendController {

    @Autowired
    public JavaMailSender javaMailSender;// 从容器中拿到 邮件发送对象

    @Value("${spring.mail.from}")
    private String emailFrom;// 从资源文件中进行读取发送方的邮件地址

//    @GetMapping("/email")
    public Boolean send(String toEmail, String Text, String Subject) {
        // new一个 简单邮件对象
        //TODO: cannot send email to XX@buaa.edu.cn
        System.out.println("sending"+toEmail+Text +Subject);
        SimpleMailMessage mail = new SimpleMailMessage();
        // 设置邮件对象的各个属性，构造成一个较为完整的邮件对象

        mail.setFrom(emailFrom);// 发邮件的邮箱地址，从资源可直接提取
        mail.setTo("904620522@qq.com");// 给谁发，通过参数进行传递
        mail.setText(Text);// 设置邮件的文本内容
        mail.setSubject(Subject);// 设置邮件的主题
        javaMailSender.send(mail);// 进行发送
        System.out.println("success");
        return true;
    }


    @GetMapping("/test_email")
    public Boolean test_send() {
        // new一个 简单邮件对象
        System.out.println("sending "+emailFrom);
        SimpleMailMessage mail = new SimpleMailMessage();
        // 设置邮件对象的各个属性，构造成一个较为完整的邮件对象
//        MimeMessage msg = javaMailSender.createMimeMessage();
//        MimeMessageHelper mail = new MimeMessageHelper( msg );

        mail.setFrom(emailFrom);// 发邮件的邮箱地址，从资源可直接提取
        mail.setTo("904620522@qq.com");// 给谁发，通过参数进行传递
        mail.setText("123");// 设置邮件的文本内容
        mail.setSubject("456");// 设置邮件的主题
        javaMailSender.send(mail);// 进行发送
        System.out.println("success");
        return true;
    }
}

