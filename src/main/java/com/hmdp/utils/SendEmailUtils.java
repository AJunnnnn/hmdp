package com.hmdp.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public class SendEmailUtils {

    private JavaMailSender javaMailSender;
    private String from;

    public SendEmailUtils(JavaMailSender javaMailSender, String from) {
        this.javaMailSender = javaMailSender;
        this.from = from;
    }

    @Async
    public void sendEmail(String to, String code) {
        // TODO 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("黑马点评验证码");
        message.setText(code);
        javaMailSender.send(message);
    }
}
