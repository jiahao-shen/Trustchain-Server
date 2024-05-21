package com.hawkeye.service;


public interface EmailSerivce {
    /**
     * 发送邮件
     *
     * @param to:      接收人
     * @param subject: 邮件主题
     * @param text:    邮件正文
     * @return: 是否发送成功
     */
    boolean send(String to, String subject, String text);

}
